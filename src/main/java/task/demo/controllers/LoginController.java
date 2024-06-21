package task.demo.controllers;

import task.demo.dto.LogInDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.demo.service.LogInService;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("${base.url}" + "/login")
public class LoginController {

    private final LogInService logInService;

    @PostMapping("")
    public String authUser(@Valid @RequestBody LogInDto logInDto) {
        return logInService.authenticate(logInDto);
    }

}
