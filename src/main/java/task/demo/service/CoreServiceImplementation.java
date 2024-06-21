package task.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import task.demo.MessageSender;
import task.demo.config.security.UserRole;
import task.demo.model.File;
import task.demo.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreServiceImplementation implements CoreService {

    private final FileRepository fileRepository;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServiceImplementation.class);
    private static final String DIRECTORY = "./src/main/resources/images";


    @Override
    @Transactional
    public void uploadFile(List<MultipartFile> files) {
        List<File> fileList = new ArrayList<>();
        if (!userService.getCurrentUser().isBlocked()) {
            files.removeIf(file -> (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("jpg")
                || !FilenameUtils.getExtension(file.getOriginalFilename()).equals("png"))
                && file.getSize() > 1e+7);
            for (MultipartFile file : files) {
                try {
                    Path path = Path.of(Paths.get(DIRECTORY).toAbsolutePath() + "/" + file.getOriginalFilename());
                    Files.write(path, file.getBytes());

                    File newFile = new File();
                    newFile.setName(file.getOriginalFilename());
                    newFile.setOwner(userService.getCurrentUser());
                    newFile.setSize(file.getSize());
                    fileRepository.save(newFile);
                    fileList.add(newFile);
                } catch (IOException e) {
                    LOGGER.warn("Upload error: " + e);
                }
            }
            try {
                messageSender.sendMessage(objectMapper.writeValueAsString(fileList), "upload");
            } catch (Exception ex) {
                LOGGER.warn("RabbitMQ error: " + ex);
            }
        } else {
            LOGGER.warn("User "
                + userService.getCurrentUser().getFirstName()
                + " " + userService.getCurrentUser().getLastName()
                + " is blocked");
        }
    }

    @Override
    public List<File> findWithFilter(Predicate predicate, String sort) {
        if (!userService.getCurrentUser().isBlocked()) {
            List<File> fileList;
            if (sort == null) {
                fileList = (predicate == null ? fileRepository.findAll() : fileRepository.findAll(predicate));
            } else {
                fileList = (predicate == null
                    ? fileRepository.findAll(Sort.by(Sort.Direction.ASC, sort))
                    : (List<File>) fileRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, sort)));
            }
            if (userService.getCurrentUser().getUserRole() == UserRole.USER) {
                fileList = fileList.stream()
                    .filter(file -> file.getOwner() == userService.getCurrentUser())
                    .toList();
            }
            return fileList;
        } else {
            LOGGER.warn("User "
                + userService.getCurrentUser().getFirstName()
                + " " + userService.getCurrentUser().getLastName()
                + " is blocked");
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Long imageId) {
        if (!userService.getCurrentUser().isBlocked()) {
            File file = fileRepository.findFileById(imageId);
            Path path = Path.of(Paths.get(DIRECTORY).toAbsolutePath() + "/" + file.getName());
            Resource fileResource = new FileSystemResource(path);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getName() + "\"");
            try {
                messageSender.sendMessage(objectMapper.writeValueAsString(file), "download");
            } catch (Exception ex) {
                LOGGER.warn("RabbitMQ error: " + ex);
            }
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
        } else {
            LOGGER.warn("User "
                + userService.getCurrentUser().getFirstName()
                + " " + userService.getCurrentUser().getLastName()
                + " is blocked");
            return ResponseEntity
                .badRequest()
                .body(new FileSystemResource(""));
        }
    }
}
