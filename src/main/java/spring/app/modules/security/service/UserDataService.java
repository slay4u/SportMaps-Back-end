package spring.app.modules.security.service;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.app.modules.security.dao.UserDataDao;
import spring.app.modules.security.domain.Status;
import spring.app.modules.security.domain.UserData;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.dto.UserDataDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDataService {

    private final UserDataDao dataDao;
    private final AuthenticationService authenticationService;

    public Status writeAction(UserAgent userAgent, String url) {
        Optional<User> user = authenticationService.getDomainUser();
        if (user.isEmpty()) {
            return Status.AUTH_REQUIRED;
        }
        UserData info = new UserData();
        boolean isOk = setTrackInfo(info, userAgent, url, user.get());
        if (isOk) {
            dataDao.save(info);
            return Status.OK;
        }
        return Status.UNKNOWN_CLIENT;
    }

    public UserDataDto getThisUserData() {
        Optional<User> wrapper = authenticationService.getDomainUser();
        if (wrapper.isEmpty()) {
            return UserDataDto.builder().build();
        }
        User user = wrapper.get();
        UserDataDto.User toSend = getUserWrapper(user);
        Optional<UserData> userDataByUser = dataDao.getUserDataByUser(user);
        if (userDataByUser.isEmpty()) {
            return UserDataDto.builder().user(toSend).build();
        } else {
            UserData userData = userDataByUser.get();
            return UserDataDto.builder()
                    .user(toSend)
                    .lastSeen(userData.getLastSeen().toString())
                    .lastUsedUrl(userData.getDestinationUrl())
                    .device(userData.getType().getName())
                    .browser(userData.getBrowser().getName())
                    .browserVersion(userData.getBrowserVersion())
                    .build();
        }
    }

    private UserDataDto.User getUserWrapper(User user) {
        UserDataDto.User toSend = new UserDataDto.User();
        toSend.firstName = user.getFirstName();
        toSend.lastName = user.getLastName();
        toSend.email = user.getEmail();
        toSend.role = user.getRole().name();
        toSend.createdAt = user.getCreated().toString();
        return toSend;
    }

    private boolean setTrackInfo(UserData info, UserAgent userAgent, String url, User user) {
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        Browser browser = userAgent.getBrowser();
        if (deviceType.equals(DeviceType.UNKNOWN) || browser.equals(Browser.UNKNOWN)) {
           return false;
        }
        // Preserve data persistence
        info.setType(deviceType);
        info.setBrowser(browser);
        Optional<String> mV = Optional.of(userAgent.getBrowserVersion().getMajorVersion());
        info.setBrowserVersion(mV.orElse(""));
        info.setUser(user);
        info.setLastSeen(LocalDateTime.now());
        info.setDestinationUrl(url);
        return true;
    }
}
