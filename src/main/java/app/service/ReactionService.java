package app.service;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import app.repository.ReactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public boolean checkUser(Answer answer, User user){
        return answer.getReactions().stream()
                .noneMatch(reaction -> reaction.getUser().getId() == user.getId());
    }
}
