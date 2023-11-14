package sport_maps.commons.util.convert;

import jakarta.persistence.EntityNotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ivan Krylosov
 */

public final class SimpleEntityConverter {

    private SimpleEntityConverter() {

    }

    /**
     * Converts object with specified type to object of another type.
     * @see Dto
     * @param convertFrom object to convert from
     * @param convertTo object to convert to
     * @return converted object
     * @param <TO> class of object to convert to
     * @param <FROM> class of object to convert from
     */

    public static <TO, FROM> TO convert(FROM convertFrom, TO convertTo) {
        List<Field> allToFields = getAllFields(new ArrayList<>(), convertTo.getClass());
        List<Field> allFromFields = getAllFields(new ArrayList<>(), convertFrom.getClass());
        if (allFromFields.isEmpty() || allToFields.isEmpty()) {
            return convertTo;
        }
        Map<String, Field> fromMap = allFromFields.stream().collect(Collectors.toMap(Field::getName, f -> f));
        for (Field tField : allToFields) {
            Dto annotation = tField.getAnnotation(Dto.class);
            if (annotation != null) {
                String property = annotation.property();
                if ("".equals(property)) {
                    property = tField.getName();
                }
                if (fromMap.containsKey(property)) {
                    Field eField = fromMap.get(property);
                    if (tField.getType().equals(eField.getType())) {
                        processNormal(convertFrom, convertTo, tField, eField);
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
        }
        BaseDto baseDto = convertTo.getClass().getAnnotation(BaseDto.class);
        if (baseDto != null) {
            processBaseDto(convertFrom, convertTo, allToFields, fromMap, baseDto);
        }
        return convertTo;
    }

    private static <TO, FROM> void processBaseDto(FROM convertFrom, TO convertTo, List<Field> allToFields, Map<String, Field> fromMap, BaseDto baseDto) {
        for (Field tField : allToFields) {
            String[] properties = baseDto.properties();
            if (properties.length != 0) {
                fromMap = fromMap.entrySet().stream()
                        .filter(e -> Arrays.stream(properties).anyMatch(p -> p.equals(e.getKey())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            String[] excludes = baseDto.exclude();
            if (excludes.length != 0) {
                fromMap = fromMap.entrySet().stream()
                        .filter(e -> Arrays.stream(excludes).noneMatch(p -> p.equals(e.getKey())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
            String tName = tField.getName();
            if (fromMap.containsKey(tName)) {
                Field eField = fromMap.get(tName);
                if (tField.getType().equals(eField.getType())) {
                    processNormal(convertFrom, convertTo, tField, eField);
                } else if (tField.getType().isEnum() && eField.getType().isAssignableFrom(String.class)) {
                    processFromString(convertFrom, convertTo, tField, eField);
                } else if (tField.getType().isAssignableFrom(String.class) && eField.getType().isEnum()) {
                    processFromEnum(convertFrom, convertTo, tField, eField);
                }
            }
        }
    }

    private static <TO, FROM> void processNormal(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        try {
            toField.set(convertTo, fromField.get(convertFrom));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static <TO, FROM> void processFromEnum(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        try {
            toField.set(convertTo, String.valueOf(fromField.get(convertFrom)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    private static <TO, FROM> void processFromString(FROM convertFrom, TO convertTo, Field toField, Field fromField) {
        setAccessed(toField, fromField);
        Object o = null;
        try {
            o = fromField.get(convertFrom);
            toField.set(convertTo, Enum.valueOf((Class<Enum>) toField.getType(), (String) o));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Enum constant not found: " + (String) o);
        }
    }

    private static void setAccessed(Field tField, Field eField) {
        tField.setAccessible(true);
        eField.setAccessible(true);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
