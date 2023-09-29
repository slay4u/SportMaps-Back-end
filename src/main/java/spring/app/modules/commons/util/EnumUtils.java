package spring.app.modules.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://stackoverflow.com/a/30212636/18763596">Source</a>
 */
public final class EnumUtils {
    public interface EnumProperty<T extends Enum<T>, U> {
        U getValue(T type);
    }

    public static <T extends Enum<T>, U> Map<U, T> createLookup(Class<T> enumTypeClass, EnumProperty<T, U> prop) {
        Map<U, T> lookup = new HashMap<>();
        for (T type : enumTypeClass.getEnumConstants()) {
            lookup.put(prop.getValue(type), type);
        }
        return Collections.unmodifiableMap(lookup);
    }
}
