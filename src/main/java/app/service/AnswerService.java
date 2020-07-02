package app.service;

import app.entity.Answer;
import app.repository.AnswerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepo answerRepo;

    public void save(Answer answer){
        answerRepo.save(answer);
    }

    public List<Answer> getAllAnswerByQId(long id){
        return answerRepo.findAllByQuestion_IdOrderByIdDesc(id);
    }

    public Optional<Answer> getAnswerById(long id){
        return answerRepo.findById(id);
    }

    public int getAnswersCount(){
        return answerRepo.findAll().size();
    }
}
