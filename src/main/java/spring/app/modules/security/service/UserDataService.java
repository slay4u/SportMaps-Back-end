package spring.app.modules.security.service;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import spring.app.modules.security.dao.UserDataDao;
import spring.app.modules.security.domain.Status;
import spring.app.modules.security.domain.UserData;
import spring.app.modules.security.domain.User;
import spring.app.modules.security.dto.UserDataDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<UserData> udToSend = formData(user);
        if (udToSend.isEmpty()) {
            return UserDataDto.builder().user(toSend).build();
        } else {
            UserData userData = udToSend.get();
            return UserDataDto.builder()
                    .user(toSend)
                    .browserVersion(userData.getBrowserVersion())
                    .lastSeen(userData.getLastSeen().toString())
                    .lastUsedUrl(userData.getDestinationUrl())
                    .browser(userData.getBrowserVersion())
                    .os(userData.getOs().getName()).build();
        }
    }

    /*public List<UserDataDto> getUsersData() {

    }*/

    private UserDataDto.User getUserWrapper(User user) {
        UserDataDto.User toSend = new UserDataDto.User();
        toSend.setFirstName(user.getFirstName());
        toSend.setLastName(user.getLastName());
        toSend.email = user.getEmail();
        toSend.role = user.getRole().name();
        toSend.createdAt = user.getCreated().toString();
        return toSend;
    }

    private Optional<UserData> formData(User user) {
        List<UserData> userData = dataDao.getUserDataByUser(user, PageRequest.of(0, 100));
        Optional<UserData> udWithLastSeen = userData.stream().filter(d -> d.getLastSeen() != null).max(Comparator.comparing(UserData::getLastSeen));
        Optional<Map.Entry<String, Long>> urlWrapper = userData.stream().collect(Collectors.groupingBy(UserData::getDestinationUrl, Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue());
        if (udWithLastSeen.isPresent()) {
            UserData ud = udWithLastSeen.get();
            if (urlWrapper.isPresent()) {
                String mostFrequentUrl = urlWrapper.get().getKey();
                ud.setDestinationUrl(mostFrequentUrl);
            } else {
                ud.setDestinationUrl("");
            }
            return Optional.of(ud);
        }
        return Optional.empty();
    }

    private boolean setTrackInfo(UserData info, UserAgent userAgent, String url, User user) {
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        if (os.equals(OperatingSystem.UNKNOWN) || browser.equals(Browser.UNKNOWN)) {
           return false;
        }
        // Preserve data persistence
        info.setOs(os);
        info.setBrowser(browser);
        Optional<String> mV = Optional.of(userAgent.getBrowserVersion().getMajorVersion());
        info.setBrowserVersion(mV.orElse(""));
        info.setUser(user);
        info.setLastSeen(LocalDateTime.now());
        info.setDestinationUrl(url);
        return true;
    }
}
