package task.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import task.demo.repository.UserRepository;
import task.demo.utils.TestUtils;


@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

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
    void before() {
        utils.addUser();
        token = utils.loginUser();
    }
    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void uploadFileTest() {
    }

    @Test
    public void getFileTest() {
    }

    @Test
    public void downloadFileTest() {

    }
}
