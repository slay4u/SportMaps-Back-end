package spring.app.modules.commons.util;

import java.util.function.Function;
import java.util.function.Predicate;

public final class ValidUtils {

    /**
     * If parses, then it surely is int
     */
    public static boolean isInteger(String str) {
        return isInBounds(str, Integer::parseInt, res -> true);
    }

    public static boolean isLong(String str) {
        return isInBounds(str, Long::parseLong, res -> true);
    }

    public static boolean isInBounds(String str, Function<String, Number> parseFunc, Predicate<Number> inBounds) {
        try {
            return inBounds.test(parseFunc.apply(str));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
