package app.service;

import app.entity.User;
import app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public boolean usernameValidation(String username){
        return username.matches("\\b[a-zA-Z][a-zA-Z0-9\\-._]{3,}\\b");
    }

    public String registerChecking(String pass, String rePass, String username){
        if(pass.equals(rePass) && usernameValidation(username)) return "ok";
        if (!usernameValidation(username)) return "wrongName";
        else return "wrongPass";
    }

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

    public List<String> getAllEmail(){
        return userRepo.getEmails();
    }

    public List<String> getAllUsernames(){
        return userRepo.getUsernames();
    }

    public void save(User user){
        userRepo.save(user);
    }
}
