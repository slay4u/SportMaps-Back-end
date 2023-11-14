package sport_maps.security.domain;

public enum Status {
    UNKNOWN_CLIENT,
    AUTH_REQUIRED,
    OK;

    public boolean isUnknown() {
        return this == UNKNOWN_CLIENT;
    }

    public boolean isAuthRequired() {
        return this == AUTH_REQUIRED;
    }

    public boolean isOk() {
        return this == OK;
    }
}
