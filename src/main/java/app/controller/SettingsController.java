package app.controller;

import app.entity.User;
import app.security.entity.CustomUserDetails;
import app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@AllArgsConstructor
@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    /**
     * http://localhost:8080/settings
     */

    @GetMapping
    ModelAndView handleSettings(Authentication auth){
        ModelAndView mav = new ModelAndView("settings");
        CustomUserDetails customUser = (CustomUserDetails) auth.getPrincipal();
        User user = customUser.getUser();
        mav.addObject("user", user);
        return mav;
    }

}
