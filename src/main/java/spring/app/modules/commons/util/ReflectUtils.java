package spring.app.modules.commons.util;

import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.exception.InternalProcessingException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Krylosov
 */

public final class ReflectUtils {

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    public static <T> Map<Field, Object> getAllValues(T t) {
        List<Field> allFields = getAllFields(new ArrayList<>(), t.getClass());
        return getAllValues(allFields, t);
    }

    public static <T> Map<Field, Object> getAllValues(List<Field> fields, T t) {
        Map<Field, Object> res = new HashMap<>();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                Object o = field.get(t);
                if (o != null) {
                    res.put(field, field.get(t));
                }
            } catch (IllegalAccessException ignored) {
                // should not get here
            }
        }
        return res;
    }

    public static <T> boolean isNull(T t) {
        return t == null || getAllValues(t).isEmpty();
    }

    public static <T> T newInstanceOf(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length != 0) {
            Object o;
            try {
                o = Arrays.stream(constructors).findAny().get().newInstance();
            } catch (Exception e) {
                throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
            }
            return clazz.cast(o);
        }
        return null;
    }

    public static <T> T newInstanceOf(Class<T> clazz, Object... args) throws Exception {
        return clazz.getConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new)).newInstance(args);
    }

    public static <T, A extends Annotation> List<Field> annotated(T t, Class<A> annotationClass) {
        return getAllFields(new ArrayList<>(), t.getClass()).stream().filter(f -> f.getAnnotation(annotationClass) != null).toList();
    }

    /**
     * Finds the generic type (parametrized type) of the field. If the field is not generic it returns Object.class.
     *
     * @param field the field to inspect
     * <a href="https://www.tabnine.com/code/java/classes/java.lang.reflect.ParameterizedType">...</a>
     */
    public static Class<?> getGenericType(Field field) {
        return getGenericTypeFromField(field, 0);
    }

    public static Class<?> getGenericType(Field field, int position) {
        return getGenericTypeFromField(field, position);
    }

    private static Class<?> getGenericTypeFromField(Field field, int position) {
        Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            Type[] res = ((ParameterizedType) generic).getActualTypeArguments();
            if (position >= res.length) {
                return Object.class;
            }
            Type actual = res[position];
            if (actual instanceof Class) {
                return (Class<?>) actual;
            } else if (actual instanceof ParameterizedType) {
                // in case of nested generics we don't go deep
                return (Class<?>) ((ParameterizedType) actual).getRawType();
            }
        }
        return Object.class;
    }

    /**
     * Attempts to find class by its name in specified package.
     * @param classname string representation of this {@link Class}
     * @param searchPackages packages to look for in
     * @return class, if found, otherwise returns null
     */
    public static Class<?> findClassByName(String classname, String[] searchPackages) {
        for (String searchPackage : searchPackages) {
            try {
                return Class.forName(searchPackage + "." + classname);
            } catch (ClassNotFoundException e) {
                //not in this package, try another
            }
        }
        return findClassByFullName(classname);
    }

    public static Class<?> findClassByFullName(String fullClassname) {
        try {
            return Class.forName(fullClassname);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * <a href="https://stackoverflow.com/a/520344/18763596">Link</a>
     * <br>
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public static Class<?>[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            throw new InternalProcessingException(e.getMessage());
        }
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[0]);
    }

    /**
     * <a href="https://stackoverflow.com/a/520344/18763596">Link</a>
     * <br>
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                Class<?> classByFullName = findClassByFullName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (classByFullName != null) {
                    classes.add(classByFullName);
                }
            }
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>[] findSubclasses(Class<T> clazz) {
        return Arrays.stream(getClasses(clazz.getPackageName())).filter(clazz::isAssignableFrom).toArray(Class[]::new);
    }
}
