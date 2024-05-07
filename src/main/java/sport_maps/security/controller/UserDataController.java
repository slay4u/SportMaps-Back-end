package sport_maps.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sport_maps.security.dto.UserDataDto;
import sport_maps.security.service.UserDataService;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/userdata")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService dataService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDataDto getThisUserData() {
        return dataService.getThisUserData();
    }
}
