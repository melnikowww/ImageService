package task.demo.controllers;

import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import task.demo.model.File;
import task.demo.service.CoreService;

import java.util.List;

@Validated
@RestController
@RequestMapping("${base.url}" + "/files")
@AllArgsConstructor
@EnableMethodSecurity
public class FileController {
    private final CoreService coreService;
    private static final String ONLY_IMAGE_OWNER = """
            @fileRepository.findById(#imageId).get().getOwner().getEmail() == authentication.getName()
        """;

    @PostMapping(path = "/upload")
    public void uploadFile(@RequestPart("files") List<MultipartFile> files) {
        coreService.uploadFile(files);
    }

    @GetMapping(path = "")
    public List<File> getFiles(
        @QuerydslPredicate(root = File.class) Predicate predicate,
        @RequestParam(required = false) String sort) {
        return coreService.findWithFilter(predicate, sort);
    }

    @PreAuthorize(ONLY_IMAGE_OWNER)
    @GetMapping(path = "/files/download/{imageId}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable Long imageId) {
        return coreService.downloadFile(imageId);
    }
}
