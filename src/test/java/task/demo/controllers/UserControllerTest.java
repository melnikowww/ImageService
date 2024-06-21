package task.demo.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import task.demo.dto.LogInDto;
import task.demo.dto.UserDto;
import task.demo.model.User;
import task.demo.repository.UserRepository;
import task.demo.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestUtils utils;
    @Value(value = "${base.url}")
    private String prefix;
    private final String baseUrl = "http://localhost:8080";
    private String token;

    @BeforeEach
    void addUser() {
        utils.addUser();
        token = utils.loginUser();
    }

    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void createUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Vladimir");
        userDto.setLastName("Melnikov");
        userDto.setEmail("melnikoffspb@ya.ru");
        userDto.setPassword("12345");

        String content = objectMapper.writeValueAsString(userDto);

        MockHttpServletResponse response = mockMvc
            .perform(post(baseUrl + prefix  + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andReturn()
            .getResponse();
        final User created = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        final User actual = userRepository.findById(created.getId()).orElseThrow();

        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(userRepository.findById(created.getId())).isPresent();
        assertThat(actual).isEqualTo(created);
    }

    @Test
    public void updateUserTest() throws Exception {
        User user = userRepository.findUserByEmail("bo0t.s@yandex.ru").orElseThrow();
        UserDto userDto = new UserDto(user.getEmail(), user.getFirstName(), "Boot3", "666777");

        MockHttpServletResponse response = mockMvc
            .perform(put(baseUrl + prefix  + "/users/" + user.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        final User expected = userRepository.findById(user.getId()).orElseThrow();
        assertThat(expected.getFirstName()).isEqualTo(userDto.getFirstName());
    }

    @Test
    public void deleteUserTest() throws Exception {
        User user = userRepository.findUserByEmail("bo0t.s@yandex.ru").orElseThrow();

        MockHttpServletResponse response = mockMvc
            .perform(delete(baseUrl + prefix  + "/users/" + user.getId())
                .header("Authorization", "Bearer " + token)
            )
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void loginUserTest() throws Exception {
        User user = userRepository.findUserByEmail("bo0t.s@yandex.ru").orElseThrow();
        LogInDto logInDto = new LogInDto(user.getEmail(), "0000");

        MockHttpServletResponse login = mockMvc
            .perform(post(baseUrl + prefix  + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logInDto)))
            .andReturn()
            .getResponse();

        assertThat(login.getStatus()).isEqualTo(200);
    }

    @Test
    public void blockUserTest() throws Exception {
        User user = userRepository.findUserByEmail("bo0t.s@yandex.ru").orElseThrow();
        assertThat(user.getBlocked()).isFalse();
        MockHttpServletResponse response = mockMvc
            .perform(post(baseUrl + prefix  + "/users/block/" + user.getId())
                .header("Authorization", "Bearer " + token))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        final User expected = userRepository.findById(user.getId()).orElseThrow();
        assertThat(expected.getBlocked()).isTrue();
    }

    @Test
    public void unblockUserTest() throws Exception {
        User user = userRepository.findUserByEmail("bo0t.s@yandex.ru").orElseThrow();

        MockHttpServletResponse response = mockMvc
            .perform(post(baseUrl + prefix  + "/users/unblock/" + user.getId())
                .header("Authorization", "Bearer " + token))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        final User expected = userRepository.findById(user.getId()).orElseThrow();
        assertThat(expected.getBlocked()).isFalse();
    }
}
