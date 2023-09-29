package spring.app.modules.commons.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;
import java.util.function.Predicate;

public final class JsonUtils {

    public static <T> T readSingleNode(JsonNode node, String target, Function<JsonNode, T> getFunc, Predicate<String> isValid) {
        JsonNode res = node.get(target);
        if (res == null || (isValid != null && !isValid.test(res.asText()))) {
            return null;
        }
        return getFunc.apply(res);
    }
}
