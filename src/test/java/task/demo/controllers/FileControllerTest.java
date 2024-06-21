package task.demo.controllers;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import task.demo.model.File;
import task.demo.repository.UserRepository;
import task.demo.utils.TestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
//        utils.addFiles();
    }
    @AfterEach
    void clean() {
        utils.clean();
    }

    @Test
    public void uploadFileTest() throws Exception {
        List<MockMultipartFile> files = new ArrayList<>();
        byte[] bytes = new byte[500];
        MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "hello.png",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
        );

        mockMvc
            .perform(
                 multipart("/files/upload")
                .file(file))
            .andExpect(status().isOk());
    }

    @Test
    public void getFileTest() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get(baseUrl + prefix  + "/files")
                .header("Authorization", "Bearer " + token))
            .andReturn()
            .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        List<File> files = Arrays.stream(objectMapper.readValue(response.getContentAsString(), File[].class)).toList();
        assertThat(files.size()).isEqualTo(0);
        assertThat(files.get(0).getName()).isEqualTo("item1");
    }

    @Test
    public void downloadFileTest() {

    }
}
