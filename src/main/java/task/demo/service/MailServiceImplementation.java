package task.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import task.demo.model.File;
import task.demo.model.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImplementation implements MailService {
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImplementation.class);
    private final JavaMailSender mailSender;

    @Override
    public void welcomeMail(String msg) {
        User user = new User();
        try {
            user = objectMapper.readValue(msg, User.class);
        } catch (Exception ex) {
            LOGGER.info("Parsing error: " + ex);
        }
        LOGGER.info("Object was parsed: " + user.getEmail());
        try {
            sendEmail(user.getEmail(),
                "Welcome!",
                "Hello there, " + user.getFirstName() + "!"
                    + Files.readString(Path.of("src/main/resources/mails/welcomeMail.txt").toAbsolutePath()));
        } catch (Exception ex) {
            LOGGER.info("Email error: " + ex);
        }
    }

    @Override
    public void uploadMail(String msg) {
        List<File> fileList = new ArrayList<>();
        Long sizeOfFile = 0L;
        try {
            fileList = Arrays.stream(objectMapper.readValue(msg, File[].class)).toList();
        } catch (Exception ex) {
            LOGGER.info("Parsing error: " + ex);
        }
        for (File file: fileList) {
            sizeOfFile = sizeOfFile + file.getSize();
        }
        User owner = fileList.get(0).getOwner();
        try {
            sendEmail(owner.getEmail(),
                "Images are successfully uploaded!",
                "Dear " + owner.getFirstName() + "!\n"
                    + "You have uploaded " + sizeOfFile + " bytes images right now!");
        } catch (Exception ex) {
            LOGGER.info("Email error: " + ex);
        }
        LOGGER.info("Objects was parsed: " + sizeOfFile + " bytes");
    }

    @Override
    public void downloadMail(String msg) {
        File file = new File();
        try {
            file = objectMapper.readValue(msg, File.class);
        } catch (Exception ex) {
            LOGGER.info("Parsing error: " + ex);
        }
        User owner = file.getOwner();
        try {
            sendEmail(owner.getEmail(),
                "Image is successfully downloaded!",
                "Dear " + owner.getFirstName() + "!\n"
                    + "You have downloaded \"" + file.getName() + "\" image!\n"
                    + "Size: " + file.getSize() + " bytes");
        } catch (Exception ex) {
            LOGGER.info("Email error: " + ex);
        }
        LOGGER.info("Object was parsed: " + file.getName());
    }

    @Override
    public void sendEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("bo0t.s@yandex.ru");
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }
}
