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

    public boolean usernameChecking(String username){
        Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
        return (username != null) && pattern.matcher(username).matches();
    }

    public void save(User user){
        userRepo.save(user);
    }
}
