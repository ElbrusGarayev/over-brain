package app.controller;

import app.entity.Follow;
import app.entity.User;
import app.security.entity.CustomUserDetails;
import app.service.FollowService;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Base64;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FollowService followService;
    private PasswordEncoder encoder;

    private static User currUser;

    public ModelAndView setProfile(String username, Authentication auth, String message){
        ModelAndView mav = new ModelAndView("profile");
        currUser = userService.getUserByUsername(username);
        CustomUserDetails userDetails;
        if (auth != null) {
            userDetails = (CustomUserDetails) auth.getPrincipal();
            if (followService.getAllFollowings(userDetails.getUser()).contains(currUser)) {
                mav.addObject("button", "Unfollow");
            } else
                mav.addObject("button", "Follow");
        }
        mav.addObject("user", currUser);
        mav.addObject("reactions", userService.getReactionsCount(currUser));
        mav.addObject("msg", message);
        return mav;
    }

    @GetMapping("{username}")
    ModelAndView handleProfile(@PathVariable String username, Authentication auth) {
        return setProfile(username, auth, "");
    }

    @PostMapping("{username}")
    RedirectView handleFollow(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        followService.save(new Follow(currUser, user));
        return new RedirectView("/" + username);
    }

    @SneakyThrows
    @PostMapping("change-photo")
    RedirectView handlePhoto(@RequestParam MultipartFile photo) {
        String newPhoto = Base64.getEncoder().encodeToString(photo.getBytes());
        currUser.setPhoto(newPhoto);
        userService.save(currUser);
        return new RedirectView("/profile/" + currUser.getUsername());
    }

    @PostMapping("change-password")
    ModelAndView handlePassword(@RequestParam String oldPass, @RequestParam String newPass, Authentication auth) {
        ModelAndView mav = new ModelAndView("profile");
        if (encoder.matches(oldPass, currUser.getPassword())){
            currUser.setPassword(encoder.encode(newPass));
            userService.save(currUser);
            return setProfile(currUser.getUsername(), auth, "");
        }
        else{
            return setProfile(currUser.getUsername(), auth, "Old password is wrong");
        }
    }
}
