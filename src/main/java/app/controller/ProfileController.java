package app.controller;

import app.entity.User;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class ProfileController {

    private final UserService userService;

    @GetMapping("{username}")
    ModelAndView handleProfile(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        ModelAndView mav = new ModelAndView("user-profile");
        mav.addObject("user", user);
        mav.addObject("reactions", userService.getReactionsCount(user));
        return mav;
    }
}
