package app.controller;

import app.cloudinary.service.CloudinaryService;
import app.entity.SocialMediaLink;
import app.entity.User;
import app.mail.sending.service.PinGenerator;
import app.mail.sending.service.Sender;
import app.model.entity.validation.FormUser;
import app.service.MediaService;
import app.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Objects;


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
    private final CloudinaryService cloudinaryService;

    private static String mailPin;
    private static String umail;
    private static User currUser;
    private static SocialMediaLink mediaLink;
    private static byte[] file;
    private static String defaultPhoto = "http://res.cloudinary.com/over-brain/image/upload/v1596114684/tduuw9lkanwzfw8v3juy.png";

    private boolean checkContentType(MultipartFile file){
        return Objects.requireNonNull(file.getContentType()).matches("image/jpeg") ||
                file.getContentType().matches("image/jpg") ||
                file.getContentType().matches("image/png");
    }

    /**
     * http://localhost:8080/user/register
     */
    @GetMapping("register")
    public String handleRegister(Authentication auth) {
        return auth instanceof AnonymousAuthenticationToken ?
                "register" : "redirect:/";
    }

    @SneakyThrows
    @PostMapping("register")
    public String handleRegister(@Valid FormUser formUser, Errors errors, User newUser,
                                 SocialMediaLink link, @RequestParam("pp") MultipartFile photo,
                                 Model model) {
        if (!errors.hasErrors()) {
            if(!checkContentType(photo))
                model.addAttribute("msg", "You can use only jpeg, jpg, png files!");
            if (userService.emailChecking(newUser.getEmail())) {
                model.addAttribute("msg", "Email is already used!");
            } else if (userService.usernameChecking(newUser.getUsername())) {
                model.addAttribute("msg", "Username is already used!");
            } else {
                file = photo.getBytes();
                newUser.setPassword(encoder.encode(newUser.getPassword()));
                currUser = newUser;
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
    String handleLogin(Authentication auth) {
        return auth instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/";
    }

    /**
     * http://localhost:8080/user/account-recovery
     */
    @GetMapping("account-recovery")
    ModelAndView handleRecovery() {
        return new ModelAndView("recovery");
    }

    @PostMapping("account-recovery")
    RedirectView handleRecovery(@RequestParam String email) {
        if (userService.emailChecking(email)) {
            umail = email;
            mailPin = String.valueOf(generator.generate());
            log.info(mailPin);
            sender.sendMail(email, mailPin);
        }
        return new RedirectView("/user/pin-checking");
    }

    @GetMapping("pin-checking")
    ModelAndView handlePin() {
        return new ModelAndView("pin");
    }

    @PostMapping("pin-checking")
    String handlePin(@RequestParam String pin, Model model) {
        if (pin.equals(mailPin)) {
            if (currUser != null) {
                if (file.length == 0)
                    currUser.setPhoto(defaultPhoto);
                else
                    currUser.setPhoto(cloudinaryService.uploadFile(file));
                userService.save(currUser);
                mediaService.save(mediaLink);
                currUser = null;
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
    RedirectView handlePassword(@RequestParam String newPass, @RequestParam String conPass) {
        User user = userService.getUserByEmail(umail);
        if (user == null) return new RedirectView("/user/login");
        else if (newPass.equals(conPass)) {
            user.setPassword(encoder.encode(newPass));
            userService.updatePass(user);
            return new RedirectView("/user/login");
        }
        return new RedirectView("/user/password-updating");
    }
}
