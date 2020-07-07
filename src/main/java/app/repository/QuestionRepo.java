package app.repository;

import app.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByIdDesc();

    List<Question> findAllByTitleContainingIgnoreCaseOrderByIdDesc(String title);
}
