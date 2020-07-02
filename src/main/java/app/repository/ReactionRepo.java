package app.repository;

import app.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, Long> {

    Reaction findByAnswer_Id(long id);

//    List<Reaction> findAllByUser_IdAndAnswer_IdAndBad(long uid, long aid);
}
