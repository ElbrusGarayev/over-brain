package app.repository;

import app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByFullnameContainingIgnoreCaseAndUsernameNotLike(String str, String username);

    @Query("select u.email from User u")
    List<String> getEmails();

    @Query("select u.username from User u")
    List<String> getUsernames();

    List<User> findAllByUsernameIsNotLike(String username);
}
