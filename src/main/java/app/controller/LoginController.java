package app.controller;

import app.entity.User;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/login")
@Log4j2
public class LoginController {

    private final UserService userService;

    /**
     * http://localhost:8080/login
     */
    @GetMapping
    String handleLogin(){
        return "login";
    }

    @PostMapping
    String handleLogin(@RequestParam String username, @RequestParam String password){
        log.info(username);
        log.info(password);
        return userService.login(username, password).isPresent() ? "redirect:/main" : "redirect:/login";
    }
}
