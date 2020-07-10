package app.repository;

import app.entity.Follow;
import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepo extends JpaRepository<Follow, Long> {

    List<Follow> findAllByWho(User user);
}
