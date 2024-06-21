package task.demo.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import task.demo.model.File;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, QuerydslPredicateExecutor<File> {
    List<File> findAll();
    List<File> findAll(Predicate predicate);
    File findFileById(Long id);
}
