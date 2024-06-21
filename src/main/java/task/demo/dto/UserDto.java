package task.demo.dto;

import task.demo.config.security.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Email
    @NotBlank
    private String email;
    @Size(min = 1)
    private String firstName;
    @Size(min = 1)
    private String lastName;
    @Size(min = 3)
    @NotBlank
    private String password;

    private UserRole role = UserRole.USER;

    public UserDto(String email, String firstName, String lastName, String password, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = (Objects.equals(role, "MODERATOR") ? UserRole.MODERATOR : UserRole.USER);
    }

    public UserDto(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
