package spring.app.modules.commons.other;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ErrorMap {

    private final Map<String, Error> errors;

    public ErrorMap() {
        errors = new HashMap<>();
    }

    public void put(String cause, String sub, String msg) {
        Error error = errors.get(cause);
        if (error != null) {
            error.put(sub, msg);
        } else {
            errors.put(cause, new Error(sub, msg));
        }
    }

    public void put(String cause, String msg) {
        errors.put(cause, new Error(msg));
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }

    public Map<String, Error> getErrors() {
        return errors;
    }

    public static class Error {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnore
        private Map<String, String> errors;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String msg;

        Error(String sub, String msg) {
            errors = new HashMap<>();
            errors.put(sub, msg);
        }

        Error(String msg) {
            this.msg = msg;
        }

        public void put(String sub, String msg) {
            errors.put(sub, msg);
        }

        @JsonAnyGetter
        @JsonSerialize
        public Map<String, String> getErrors() {
            return errors;
        }

        public String getMsg() {
            return msg;
        }

        // Logging
        @Override
        public String toString() {
            return errors == null ? msg : formatMap();
        }

        private String formatMap() {
            return errors.keySet().stream()
                    .map(key -> key + "= " + errors.get(key))
                    .collect(Collectors.joining(", ", "{", "}"));
        }
    }
}
