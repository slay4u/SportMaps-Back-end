package spring.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class PingController {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public ResponseEntity<String> getConnection() {
        return ResponseEntity.ok("Application is running OK!");
    }
}
