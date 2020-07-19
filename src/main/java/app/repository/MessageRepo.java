package app.repository;

import app.entity.Message;
import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("select m from Message m where (m.who = ?1 and m.whom = ?2) or (m.who = ?3 and m.whom = ?4) order by m.id asc")
    List<Message> getChat(User who, User whom, User who1, User whom1);
}
