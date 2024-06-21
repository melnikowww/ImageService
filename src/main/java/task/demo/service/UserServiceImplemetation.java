package task.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import task.demo.MessageSender;
import task.demo.config.security.UserRole;
import task.demo.dto.UserDto;
import task.demo.model.User;
import task.demo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImplemetation implements UserService {

    private final PasswordEncoder encoder;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplemetation.class);

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(userDto.getRole());
        user.setBlocked(false);
        try {
            messageSender.sendMessage(objectMapper.writeValueAsString(user), "welcome");
        } catch (Exception ex) {
            LOGGER.warn("RabbitMQ error: " + ex);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUserRole(user.getUserRole());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        return userRepository
            .findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
            .orElseThrow();
    }

    @Override
    public void blockUser(Long id) {
        if (getCurrentUser().getUserRole() == UserRole.MODERATOR) {
            User user = userRepository.findById(id).orElseThrow();
            user.setBlocked(true);
            LOGGER.warn("User "
                + getCurrentUser().getFirstName()
                + " " + getCurrentUser().getLastName()
                + " was blocked");
            userRepository.save(user);
        }
    }

    @Override
    public void unblockUser(Long id) {
        if (getCurrentUser().getUserRole() == UserRole.MODERATOR) {
            User user = userRepository.findById(id).orElseThrow();
            user.setBlocked(false);
            LOGGER.warn("User "
                + getCurrentUser().getFirstName()
                + " " + getCurrentUser().getLastName()
                + " was unblocked");
            userRepository.save(user);
        }
    }
}
