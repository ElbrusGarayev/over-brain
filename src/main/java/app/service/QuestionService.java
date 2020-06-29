package app.service;

import app.entity.Question;
import app.repository.QuestionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepo questionRepo;

    public void save(Question question){
        questionRepo.save(question);
    }
}
