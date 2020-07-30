package app.controller;

import app.cloudinary.service.CloudinaryService;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FollowService followService;
    private final CloudinaryService cloudinaryService;
    private PasswordEncoder encoder;

    private static User currUser;

    private boolean checkContentType(MultipartFile file){
        return Objects.requireNonNull(file.getContentType()).matches("image/jpeg") ||
                file.getContentType().matches("image/jpg") ||
                file.getContentType().matches("image/png");
    }

    private ModelAndView setProfile(String username, Authentication auth, String message){
        ModelAndView mav = new ModelAndView("profile");
        User user = userService.getUserByUsername(username);
        CustomUserDetails userDetails;
        if (auth != null) {
            userDetails = (CustomUserDetails) auth.getPrincipal();
            currUser = userDetails.getUser();
            if (followService.getAllFollowings(currUser).contains(user)) {
                mav.addObject("button", "Unfollow");
            } else
                mav.addObject("button", "Follow");
        }
        mav.addObject("user", user);
        mav.addObject("reactions", userService.getReactionsCount(user));
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
        if (!followService.getAllFollowings(currUser).contains(user)) {
            followService.follow(new Follow(currUser, user));
        } else {
            followService.unfollow(currUser, user);
        }
        return new RedirectView("/profile/" + username);
    }

    @SneakyThrows
    @PostMapping("change-photo")
    RedirectView handlePhoto(@RequestParam MultipartFile photo) {
        if (!photo.isEmpty() && checkContentType(photo)){
            currUser.setPhoto(cloudinaryService.uploadFile(photo.getBytes()));
            userService.save(currUser);
        }
        return new RedirectView("/profile/" + currUser.getUsername());
    }

    @PostMapping("change-password")
    ModelAndView handlePassword(@RequestParam String oldPass, @RequestParam String newPass, Authentication auth) {
        if (encoder.matches(oldPass, currUser.getPassword())){
            if (newPass.length() >= 8){
                currUser.setPassword(encoder.encode(newPass));
                userService.save(currUser);
                return setProfile(currUser.getUsername(), auth, "");
            }
            else
                return setProfile(currUser.getUsername(), auth, "New password length shouldn't be less than 8");
        }
        else{
            return setProfile(currUser.getUsername(), auth, "Old password is wrong");
        }
    }
}
