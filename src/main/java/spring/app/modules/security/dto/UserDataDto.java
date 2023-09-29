package spring.app.modules.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.commons.other.PersonDto;

@Getter
@Setter
@Builder
@Jacksonized
public class UserDataDto {
    public String lastSeen;
    public String lastUsedUrl;
    public String os;
    public String browser;
    public String browserVersion;
    public User user;

    public static class User extends PersonDto {
        public String email;
        public String createdAt;
        public String role;
    }
}
