package sport_maps.security.domain;

public enum Role {
    USER, ADMIN;

    public boolean isUser () {
        return this == USER;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
