package app.repository;

import app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findAllByFullnameContainingIgnoreCase(String str, Pageable pageable);

    List<User> findAllByFullnameContainingIgnoreCase(String str);

    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
