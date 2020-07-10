package app.service;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import app.repository.ReactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ReactionService {

    private final ReactionRepo reactionRepo;

    public void save(Reaction reaction){
        reactionRepo.save(reaction);
    }

    public Optional<Reaction> getByAnswerIdAndUser(Answer answer, User user){
        return reactionRepo.findByAnswerAndUser(answer, user);
    }
}
