package app.service;

import app.entity.User;
import app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public User getUser(String email){
        return userRepo.findByEmail(email).get();
    }

    public void updatePass(User user){
        userRepo.save(user);
    }
    
    public Optional<User> login(String username, String password){
        return userRepo.findAll().stream()
                .filter(u -> u.getUsername().equals(username) &&
                        u.getPassword().equals(password))
                .findAny();
    }
}
