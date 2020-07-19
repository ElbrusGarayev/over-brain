package app.repository;

import app.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {

    Page<Question> findAllByTitleContainingIgnoreCaseOrderByIdDesc(String search, Pageable pageable);

    List<Question> findAllByTitleContainingIgnoreCaseOrderByIdDesc(String title);
}
