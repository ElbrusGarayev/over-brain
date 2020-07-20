package app.controller;

import app.entity.Message;
import app.entity.User;
import app.security.entity.CustomUserDetails;
import app.service.FollowService;
import app.service.MessageService;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final UserService userService;
    private final FollowService followService;
    private final MessageService messageService;

    private static User who;
    private static User whom;

    @GetMapping
    ModelAndView handleChat(Authentication auth){
        ModelAndView mav = new ModelAndView("chat");
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        who = userDetails.getUser();
        mav.addObject("contacts", followService.getAllFollowings(who));
        return mav;
    }

    @GetMapping("{username}")
    ModelAndView handleChat(@PathVariable String username, Authentication auth){
        ModelAndView mav = new ModelAndView("chat");
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        who = userDetails.getUser();
        whom = userService.getUserByUsername(username);
        mav.addObject("contacts", followService.getAllFollowings(who));
        mav.addObject("whom", whom);
        mav.addObject("who", who);
        mav.addObject("messages", messageService.getAll(who, whom));
        return mav;
    }

    @PostMapping("{username}")
    RedirectView handleChat(@PathVariable String username, @RequestParam Optional<String> content){
        String date = LocalDateTime.now().format(formatter);
        int trimContentLen = content.get().trim().length();
        if (trimContentLen > 0) messageService.send(new Message(content.get(), date, who, whom));
        return new RedirectView("/chat/" + username);
    }
}
