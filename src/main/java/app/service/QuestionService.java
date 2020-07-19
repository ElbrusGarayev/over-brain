package app.service;

import app.entity.Question;
import app.repository.QuestionRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepo questionRepo;

    public void save(Question question){
        questionRepo.save(question);
    }

    public Page<Question> getAll(String search, Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.orElse(0), 5,
                Sort.Direction.ASC, "id");
        return questionRepo.findAllByTitleContainingIgnoreCaseOrderByIdDesc(search, pageable);
    }

    public Optional<Question> getQuestionById(long id){
        return questionRepo.findById(id);
    }

    public List<Question> getAllByTitle(String str){
        return questionRepo.findAllByTitleContainingIgnoreCaseOrderByIdDesc(str);
    }
}
