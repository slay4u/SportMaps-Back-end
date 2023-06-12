package spring.app.modules.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.app.modules.security.dto.UserDataDto;
import spring.app.modules.security.service.UserDataService;

@RestController
@RequestMapping("/sport-maps/v1/userdata")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService dataService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDataDto getSingleUserData() {
        return dataService.getThisUserData();
    }
}
