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

    List<User> findAllByFullnameContainingIgnoreCaseAndUsernameNotLike(String str, String username);

    List<User> findAllByUsernameIsNotLike(String username);

    Optional<User> findByUsername(String username);
}
