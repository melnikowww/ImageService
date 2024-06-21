package task.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import task.demo.config.security.JwtUtils;
import task.demo.dto.LogInDto;

@Service
@RequiredArgsConstructor
public class LogInServiceImplementation implements LogInService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @Override
    public String authenticate(LogInDto logInDto) {
        var auth = new UsernamePasswordAuthenticationToken(
            logInDto.getEmail(), logInDto.getPassword());
        authenticationManager.authenticate(auth);
        return jwtUtils.generateToken(logInDto);
    }
}
