package task.demo.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import task.demo.config.security.UserRole;
import task.demo.dto.LogInDto;
import task.demo.dto.UserDto;
import task.demo.model.File;
import task.demo.model.User;
import task.demo.repository.FileRepository;
import task.demo.repository.UserRepository;
import task.demo.service.CoreService;
import task.demo.service.LogInService;
import task.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.amqp.rabbit.connection.NodeLocator.LOGGER;


@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LogInService logInService;
    @Autowired
    private CoreService coreService;
    private final String email = "bo0t.s@yandex.ru";
    private final String password = "0000";

    public User addUser() {
        UserDto userDto = new UserDto("bo0t.s@yandex.ru", "Spring", "Boot", "0000", UserRole.MODERATOR);
        if (userRepository.findUserByEmail(userDto.getEmail()).isEmpty()) {
            return userService.createUser(userDto);
        }
        return new User();
    }

    public void addUsers() {
        UserDto userDto = new UserDto("senya@mail.ru", "Semyon", "Semyonich", "sem777");
        userService.createUser(userDto);

        UserDto userDto1 = new UserDto("petr@ya.ru", "Petr", "Petrovich", "petr13");
        userService.createUser(userDto1);
    }

    public String loginUser() {
        LogInDto logInDto = new LogInDto(email, password);
        return logInService.authenticate(logInDto);
    }

    public void blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userService.blockUser(user.getId());
    }

    public void unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setBlocked(false);
        userRepository.save(user);
    }

    public void addFiles() {
        List<MultipartFile> files = new ArrayList<>();
        User user = new User();
        files.add(new MockMultipartFile("files", "item1.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[500]));
        files.add(new MockMultipartFile("files", "item2.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[500]));
        files.add(new MockMultipartFile("files", "item3.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[500]));
        for (MultipartFile file : files) {
            try {
                Path path = Path.of(
                    Paths.get("src/test/resources/uploadedTestFiles").toAbsolutePath()
                        + "/" + file.getOriginalFilename());
                Files.write(path, file.getBytes());

                File newFile = new File();
                newFile.setName(file.getOriginalFilename());
                newFile.setOwner(user);
                newFile.setSize(file.getSize());
                fileRepository.save(newFile);
            } catch (IOException e) {
                LOGGER.warn("Upload error: " + e);
            }
        }
    }

    public void clean() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
    }

}
