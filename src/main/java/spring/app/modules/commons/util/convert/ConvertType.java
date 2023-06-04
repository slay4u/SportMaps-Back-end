package spring.app.modules.commons.util.convert;

public enum ConvertType {
    STRING,
    ENUM,
    ORDINAL;

    ConvertType() {

    }

    boolean isSTRING() {
        return this == STRING;
    }

    boolean isENUM() {
        return this == ENUM;
    }
}
