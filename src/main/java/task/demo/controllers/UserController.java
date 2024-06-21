package task.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import task.demo.dto.UserDto;
import task.demo.model.User;
import task.demo.repository.UserRepository;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.demo.service.UserService;

import java.util.List;

@Validated
@RestController()
@RequestMapping("${base.url}" + "/users")
@AllArgsConstructor
@EnableMethodSecurity
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @GetMapping(
        path = "/{id}",
        produces = "application/json")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping(
        path = "",
        produces = "application/json")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping(path = "/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userDto, id);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping(path = "/block/{id}")
    public void blockUser(@PathVariable Long id) {
        userService.blockUser(id);
    }

    @PostMapping(path = "/unblock/{id}")
    public void unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
    }
}
