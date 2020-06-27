package app.controller;

import app.entity.User;
import app.mail.sending.service.PinGenerator;
import app.mail.sending.service.Sender;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final Sender sender;
    private final PinGenerator generator;

    private static String mailPin;
    private static String umail;

    /**
     * http://localhost:8080/user/login
     */
    @GetMapping("login")
    String handleLogin() {
        return "login";
    }

    @PostMapping("login")
    String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
        if(userService.login(username, password).isPresent()){
            return "redirect:/main";
        }
        else
         model.addAttribute("message", "Username or Password is wrong!");
        return "login";
    }

    /**
     * http://localhost:8080/user/account-recovery
     */
    @GetMapping("account-recovery")
    String handleRecovery() {
        return "recovery";
    }

    @PostMapping("account-recovery")
    String handleRecovery(@RequestParam String email, Model model) {
        if(userService.getAllEmail().contains(email)){
            umail = email;
            mailPin = String.valueOf(generator.generate());
            log.info(mailPin);
            sender.sendMail(email, "Your Password Changing PIN", String.valueOf(mailPin));
            return "redirect:/user/pin-checking";
        }else{
            model.addAttribute("message", "Are you sure you have an account? :(");
        }
        return "recovery";
    }

    @GetMapping("pin-checking")
    String handlePin() {
        return "pin";
    }

    @PostMapping("pin-checking")
    String handlePin(@RequestParam String pin, Model model) {
        if (pin.equals(mailPin)) {
            return "redirect:/user/password-updating";
        } else {
            model.addAttribute("message", "Pins didn't match!");
            return "pin";
        }
    }

    @GetMapping("password-updating")
    String handlePassword(){
        return "password";
    }

    @PostMapping("password-updating")
    String handlePassword(@RequestParam String newPass, @RequestParam String conPass, Model model){
        if(newPass.equals(conPass)){
            User user = userService.getUser(umail);
            user.setPassword(newPass);
            userService.updatePass(user);
            return "redirect:/user/login";
        }else
            model.addAttribute("message", "Passwords didn't match!");
        return "password";
    }
}
