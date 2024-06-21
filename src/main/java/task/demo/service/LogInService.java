package task.demo.service;

import task.demo.dto.LogInDto;

public interface LogInService {
    String authenticate(LogInDto logInDto);
}
