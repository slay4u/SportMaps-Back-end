package spring.app.modules.commons.util.convert;

import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.exception.InternalProcessingException;
import spring.app.modules.commons.exception.SMBusinessLogicException;
import spring.app.modules.commons.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * FIXME: this should work without any additional annotations.
 * @author Ivan Krylosov
 */
public final class SimpleEntityConverter {

    private SimpleEntityConverter() {

    }

    /**
     * Converts object with specified type to object of another type.
     * @see BaseDto
     * @param convertFrom object to convert from
     * @param convertTo object to convert to
     * @return converted object
     * @param <TO> class of object to convert to
     * @param <FROM> class of object to convert from
     *
     * @apiNote map conversions currently are unable with the usage of {@link Dto}.
     * We don't need to manually convert string -> enum, enum -> string anymore as it's done as a part of Jackson serialization / deserialization.
     */

    public static <TO, FROM> TO convert(FROM convertFrom, TO convertTo) {
        List<Field> allToFields = ReflectUtils.annotated(convertTo.getClass(), Dto.class);
        List<Field> allFromFields = ReflectUtils.getAllFields(new ArrayList<>(), convertFrom.getClass());
        if (allFromFields.isEmpty()) {
            return convertTo;
        }
        Map<String, Field> fromMap = allFromFields.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        for (Field tField : allToFields) {
            Dto annotation = tField.getAnnotation(Dto.class);
            if (annotation != null) {
                //processDto(convertFrom, convertTo, fromMap, tField, annotation);
            }
        }
        BaseDto baseDto = convertTo.getClass().getAnnotation(BaseDto.class);
        if (baseDto != null) {
            processBaseDto(convertFrom, convertTo, ReflectUtils.getAllFields(new ArrayList<>(), convertTo.getClass()), fromMap, baseDto);
        }
        return convertTo;
    }

    @Deprecated(forRemoval = true)
    private static <TO, FROM> void processDto(FROM convertFrom, TO convertTo, Map<String, Field> fromMap, Field tField, Dto annotation) {
        String property = annotation.property();
        if ("".equals(property)) {
            property = tField.getName();
        }
        if (fromMap.containsKey(property)) {
            Field eField = fromMap.get(property);
            if (tField.getType().equals(eField.getType())) {
                if (ReflectUtils.getGenericType(eField) == Object.class && ReflectUtils.getGenericType(tField) == Object.class) {
                    processNormal(convertFrom, convertTo, tField, eField);
                } else {
                    processGenericCollection(convertFrom, convertTo, tField, eField);
                }
            } else {
                if (annotation.value().isSTRING()) {
                    processFromString(convertFrom, convertTo, tField, eField);
                }
                if (annotation.value().isENUM()) {
                    processFromEnum(convertFrom, convertTo, tField, eField);
                }
            }
        }
    }

    private static <TO, FROM> void processBaseDto(FROM convertFrom, TO convertTo, List<Field> allToFields, Map<String, Field> fromMap, BaseDto baseDto) {
        for (Field tField : allToFields) {
            String[] properties = baseDto.properties();
            if (properties.length != 0) {
                Predicate<Map.Entry<String, Field>> include = e -> Arrays.stream(properties).anyMatch(p -> p.equals(e.getKey()));
                fromMap = getStringFieldMap(fromMap, include);
            }
            String[] excludes = baseDto.exclude();
            if (excludes.length != 0) {
                Predicate<Map.Entry<String, Field>> exclude = e -> Arrays.stream(excludes).noneMatch(p -> p.equals(e.getKey()));
                fromMap = getStringFieldMap(fromMap, exclude);
            }
            String tName = tField.getName();
            if (fromMap.containsKey(tName)) {
                Field eField = fromMap.get(tName);
                if (tField.getType().equals(eField.getType())) {
                    if (ReflectUtils.getGenericType(eField) == Object.class && ReflectUtils.getGenericType(tField) == Object.class) {
                        processNormal(convertFrom, convertTo, tField, eField);
                    } else {
                        processGenericCollection(convertFrom, convertTo, tField, eField);
                    }
                }
            }
        }
    }

    private static Map<String, Field> getStringFieldMap(Map<String, Field> fromMap, Predicate<Map.Entry<String, Field>> include) {
        fromMap = fromMap.entrySet().stream()
                .filter(include)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return fromMap;
    }

    @SuppressWarnings("all")
    private static <TO, FROM> void processGenericCollection(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        Class<?> toGeneric = ReflectUtils.getGenericType(toField);
        Class<?> fromGeneric = ReflectUtils.getGenericType(fromField);
        Object obj = tryGetObject(convertFrom, fromField);
        if (obj == null) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR);
        } else if (toGeneric.equals(fromGeneric)) {
            Class<?> to2Generic = ReflectUtils.getGenericType(toField, 1);
            Class<?> from2Generic = ReflectUtils.getGenericType(fromField, 1);
            if (to2Generic != Object.class && from2Generic != Object.class) {
                if (toField.getType().isAssignableFrom(Map.class)) {
                    processMap(convertFrom, convertTo, toField, fromField, (Map) obj, k -> ((Map.Entry) k).getKey());
                }
            } else {
                processNormal(convertFrom, convertTo, toField, fromField);
            }
        }
    }

    @Deprecated
    @SuppressWarnings("all")
    private static <TO, FROM> void processCollectionStringEnum(FROM convertFrom, TO convertTo, Field toField, Field fromField, Function mapFunc, Object obj) {
        // stub: manually checking for collection type
        if (toField.getType().isAssignableFrom(Set.class)) {
            processCollectionStringEnum(convertFrom, convertTo, toField, fromField, (Collection) obj, Collectors.toSet(), Set.class, mapFunc);
        }
        if (toField.getType().isAssignableFrom(List.class)) {
            processCollectionStringEnum(convertFrom, convertTo, toField, fromField, (Collection) obj, Collectors.toList(), List.class, mapFunc);
        }
    }

    @SuppressWarnings("all")
    private static <TO, FROM> void processMap(FROM convertFrom, TO convertTo, Field toField, Field fromField, Map obj, Function keyFunc) {
        Class<?> to2Generic = ReflectUtils.getGenericType(toField, 1);
        Class<?> from2Generic = ReflectUtils.getGenericType(fromField, 1);
        if (to2Generic != Object.class && to2Generic == from2Generic) {
            processMap(convertFrom, convertTo, toField, fromField, obj, keyFunc, v -> ((Map.Entry) v).getValue());
        }
    }


    private static <FROM> Object tryGetObject(FROM convertFrom, Field fromField) {
        Object obj;
        fromField.setAccessible(true);
        try {
            obj = fromField.get(convertFrom);
        } catch (IllegalAccessException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
        return obj;
    }

    @SuppressWarnings("all")
    private static  <TO, FROM> void processMap(FROM convertFrom, TO convertTo, Field toField, Field fromField, Map obj, Function<Object, Object> keyFunc, Function<Object, Object> valueFunc) {
        Map c = (Map) obj.entrySet().stream().filter(Objects::nonNull).collect(Collectors.toMap(keyFunc, valueFunc));
        try {
            fromField.set(convertFrom, c);
        } catch (Exception e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
        processNormal(convertFrom, convertTo, toField, fromField);
    }

    @Deprecated
    @SuppressWarnings("all")
    private static <TO, FROM> void processCollectionStringEnum(FROM convertFrom, TO convertTo, Field toField, Field fromField, Collection obj, Collector<Object, ?, ?> collector, Class<?> toCollection, Function mapFunc) {
        Collection c = (Collection) obj.stream()
                .filter(Objects::nonNull).map(mapFunc)
                .collect(collector);
        try {
            fromField.set(convertFrom, toCollection.cast(c));
        } catch (Exception e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
        processNormal(convertFrom, convertTo, toField, fromField);
    }

    private static <TO, FROM> void processNormal(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        try {
            toField.set(convertTo, fromField.get(convertFrom));
        } catch (IllegalAccessException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
    }

    @Deprecated
    private static <TO, FROM> void processFromEnum(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        try {
            toField.set(convertTo, String.valueOf(fromField.get(convertFrom)));
        } catch (IllegalAccessException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        }
    }

    @Deprecated
    @SuppressWarnings("all")
    private static <TO, FROM> void processFromString(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        Object o = null;
        try {
            o = fromField.get(convertFrom);
            toField.set(convertTo, Enum.valueOf((Class<Enum>) toField.getType(), (String) o));
        } catch (IllegalAccessException e) {
            throw new InternalProcessingException(SystemConstants.INTERNAL_ERROR, e);
        } catch (IllegalArgumentException e) {
            throw new SMBusinessLogicException(SystemConstants.ENUM_ERROR + (String) o);
        }
    }

    private static void setAccessed(Field tField, Field eField) {
        tField.setAccessible(true);
        eField.setAccessible(true);
    }
}
