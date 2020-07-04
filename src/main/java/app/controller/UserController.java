package app.controller;

import app.entity.SocialMediaLink;
import app.entity.User;
import app.mail.sending.service.PinGenerator;
import app.mail.sending.service.Sender;
import app.service.MediaService;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Optional;


@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MediaService mediaService;
    private final Sender sender;
    private final PinGenerator generator;

    private static String mailPin;
    private static String umail;
    private static User user;
    private static SocialMediaLink mediaLink;

    /**
     * http://localhost:8080/user/register
     */
    @GetMapping("register")
    ModelAndView handleRegister() {
        ModelAndView mav = new ModelAndView("register");
        return mav;
    }

    @SneakyThrows
    @PostMapping("register")
    String handleRegister(Model model, User newUser, SocialMediaLink link,
                          @RequestParam("pp") MultipartFile photo, @RequestParam String rePass) {
        String regChecking = userService.registerChecking(newUser.getPassword(), rePass, newUser.getUsername());
        if (userService.getAllEmail().contains(newUser.getEmail())) {
            model.addAttribute("mailMsg", "Email is already used!");
            return "register";
        }
        if (userService.getAllUsernames().contains(newUser.getUsername())) {
            model.addAttribute("nameMsg", "Username is already used!");
            return "register";
        }
        if (regChecking.equals("ok")) {
            newUser.setPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
            user = newUser;
            mailPin = String.valueOf(generator.generate());
            log.info(mailPin);
            sender.sendMail(newUser.getEmail(), mailPin);
            link.setUser(newUser);
            mediaLink = link;
            return "redirect:/user/pin-checking";
        }
        if (regChecking.equals("wrongName")) {
            model.addAttribute("nameMsg", "Username is wrong!");
            return "register";
        }
        model.addAttribute("passMsg", "Passwords didn't match!");
        return "register";
    }

    /**
     * http://localhost:8080/user/login
     */
    @GetMapping("login")
    ModelAndView handleLogin() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @PostMapping("login")
    String handleLogin(User user, Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Optional<User> current = userService.login(user.getUsername(), user.getPassword());
        if (current.isPresent()) {
            session.setAttribute("user", current.get());
            return "redirect:/main";
        }
        model.addAttribute("message", "Username or Password is wrong!");
        return "login";
    }

    /**
     * http://localhost:8080/user/account-recovery
     */
    @GetMapping("account-recovery")
    ModelAndView handleRecovery() {
        ModelAndView mav = new ModelAndView("recovery");
        return mav;
    }

    @PostMapping("account-recovery")
    String handleRecovery(@RequestParam String email, Model model) {
        if (userService.getAllEmail().contains(email)) {
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
        ModelAndView mav = new ModelAndView("pin");
        return mav;
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
        ModelAndView mav = new ModelAndView("password");
        return mav;
    }

    @PostMapping("password-updating")
    RedirectView handlePassword(@RequestParam String newPass, @RequestParam String conPass, Model model) {
        if (newPass.equals(conPass)) {
            User user = userService.getUser(umail);
            user.setPassword(newPass);
            userService.updatePass(user);
            return new RedirectView("/user/login");
        }
        model.addAttribute("message", "Passwords didn't match!");
        return new RedirectView("/user/password-updating");
    }

    @GetMapping("logout")
    RedirectView handleLogout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute("user");
        return new RedirectView("/user/login");
    }
}
