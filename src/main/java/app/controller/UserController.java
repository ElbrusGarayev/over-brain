package app.controller;

import app.entity.SocialMediaLink;
import app.entity.User;
import app.mail.sending.service.PinGenerator;
import app.mail.sending.service.Sender;
import app.model.entity.validation.FormUser;
import app.service.MediaService;
import app.service.UserService;
import org.springframework.validation.Errors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;


@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MediaService mediaService;
    private final Sender sender;
    private final PinGenerator generator;
    private final PasswordEncoder encoder;

    private static String mailPin;
    private static String umail;
    private static User user;
    private static SocialMediaLink mediaLink;

    public String getPhotoString(MultipartFile photo) throws IOException {
       return Base64.getEncoder().encodeToString(photo.getBytes());
    }

    /**
     * http://localhost:8080/user/register
     */
    @GetMapping("register")
    public ModelAndView handleRegister() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("formUser", new FormUser());
        return mav;
    }

    @SneakyThrows
    @PostMapping("register")
    public String handleRegister(@Valid FormUser formUser, Errors errors, User newUser, SocialMediaLink link,
                                @RequestParam("pp") MultipartFile photo, Model model) {
        if (!errors.hasErrors()) {
            if (userService.emailChecking(newUser.getEmail())) {
                model.addAttribute("msg", "Email is already used!");
            } else if (userService.usernameChecking(newUser.getUsername())) {
                model.addAttribute("msg", "Username is already used!");
            } else {
                newUser.setPhoto(getPhotoString(photo));
                newUser.setPassword(encoder.encode(newUser.getPassword()));
                user = newUser;
                mailPin = String.valueOf(generator.generate());
                log.info(mailPin);
                sender.sendMail(newUser.getEmail(), mailPin);
                link.setUser(newUser);
                mediaLink = link;
                return "redirect:/user/pin-checking";
            }
        }
        return "register";
    }

    /**
     * http://localhost:8080/user/login
     */
    @GetMapping("login")
    ModelAndView handleLogin() {
        return new ModelAndView("login");
    }

    /**
     * http://localhost:8080/user/account-recovery
     */
    @GetMapping("account-recovery")
    ModelAndView handleRecovery() {
        return new ModelAndView("recovery");
    }

    @PostMapping("account-recovery")
    String handleRecovery(@RequestParam String email, Model model) {
        if (userService.emailChecking(email)) {
            umail = email;
            mailPin = String.valueOf(generator.generate());
            log.info(mailPin);
            sender.sendMail(email, mailPin);
            return "redirect:/user/pin-checking";
        }
        model.addAttribute("message", "Are you sure you have an account? :(");
        return "recovery";
    }

    @GetMapping("pin-checking")
    ModelAndView handlePin() {
        return new ModelAndView("pin");
    }

    @PostMapping("pin-checking")
    String handlePin(@RequestParam String pin, Model model) {
        if (pin.equals(mailPin)) {
            if (user != null) {
                userService.save(user);
                mediaService.save(mediaLink);
                return "redirect:/user/login";
            }
            return "redirect:/user/password-updating";
        }
        model.addAttribute("message", "Pins didn't match!");
        return "pin";
    }

    @GetMapping("password-updating")
    ModelAndView handlePassword() {
        return new ModelAndView("password");
    }

    @PostMapping("password-updating")
    RedirectView handlePassword(@RequestParam String newPass, @RequestParam String conPass, Model model) {
        if (newPass.equals(conPass)) {
            User user = userService.getUserByEmail(umail);
            user.setPassword(encoder.encode(newPass));
            userService.updatePass(user);
            return new RedirectView("/user/login");
        }
        return new RedirectView("/user/password-updating");
    }
}
