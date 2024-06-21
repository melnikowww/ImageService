package task.demo.service;

import com.querydsl.core.types.Predicate;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import task.demo.model.File;

import java.util.List;

public interface CoreService {
    void uploadFile(List<MultipartFile> files);
    List<File> findWithFilter(Predicate predicate, @RequestParam String sort);
    ResponseEntity<Resource> downloadFile(Long imageId);
}
