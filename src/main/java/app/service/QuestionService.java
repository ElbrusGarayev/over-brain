package app.service;

import app.entity.Question;
import app.repository.QuestionRepo;
import lombok.AllArgsConstructor;
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

    public List<Question> getAll(){
        return questionRepo.findAllByOrderByIdDesc();
    }

    public Optional<Question> getQuestionById(long id){
        return questionRepo.findById(id);
    }

    public List<Question> getAllByTitle(String str){
        return questionRepo.findAllByTitleContainingIgnoreCase(str);
    }
}
