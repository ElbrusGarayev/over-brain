package app.controller;

import app.entity.User;
import app.mail.sending.service.Sender;
import app.mail.sending.service.PinGenerator;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/forgot")
public class ForgotController {

    private final Sender sender;
    private final PinGenerator generator;
    private final UserService userService;

    private static String mailPin;
    private static String umail;

    @GetMapping
    String handleForgot() {
        return "forgot";
    }

    @PostMapping
    String handlePin(@RequestParam String email, @RequestParam String pin, HttpServletRequest req) {
        String click = req.getParameter("button");
        if (click.equals("send")) {
            umail = email;
            mailPin = String.valueOf(generator.generate());
            log.info(mailPin);
            sender.sendMail(email, "Your Password Changing PIN", String.valueOf(mailPin));
        } else if (click.equals("submit")) {
            log.info(mailPin);
            log.info("log" + pin + "log");
            if (pin.equals(mailPin)){
                User user = userService.getUser(umail);
                user.setPassword("123456");
                userService.updatePass(user);
            }else
                log.info("Pins doesn't match!!!");
        }
        return "redirect:/forgot";
    }
}
