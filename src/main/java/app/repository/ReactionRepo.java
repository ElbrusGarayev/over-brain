package app.repository;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByAnswerAndUser(Answer answer, User user);

}
