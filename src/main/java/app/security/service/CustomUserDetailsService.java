package app.security.service;

import app.entity.User;
import app.repository.UserRepo;
import app.security.entity.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final HttpServletRequest request;
    private final LoginAttemptService attemptService;

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (attemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
        try{
            Optional<User> user = userRepo.findByUsername(username);
            user.orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
            return user.map(CustomUserDetails::new).get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
