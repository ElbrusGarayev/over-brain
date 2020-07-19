package app.service;

import app.entity.User;
import app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public boolean emailChecking(String email){
        return userRepo.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean usernameChecking(String username){
        return userRepo.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).get();
    }

    public void updatePass(User user) {
        userRepo.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepo.findAll().stream()
                .filter(u -> u.getUsername().equals(username) &&
                        u.getPassword().equals(password))
                .findAny();
    }

    public Page<User> getAll(String str, Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.orElse(0), 1,
                Sort.Direction.ASC, "id");
        return userRepo.findAllByFullnameContainingIgnoreCase(str, pageable);
    }

    public List<User> getUsersBy(String str){
        return userRepo.findAllByFullnameContainingIgnoreCase(str);
    }

    public long getReactionsCount(User user){
        return user.getAnswers()
                .stream()
                .mapToLong(answer -> answer.getReactions().stream()
                        .filter(reaction -> reaction.isStatus()).count()).sum();
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
