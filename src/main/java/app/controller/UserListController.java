package app.controller;

import app.entity.User;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class UserListController {

    private final UserService userService;
    private static User user;

    /**
     * http://localhost:8080/users
     */
    @GetMapping("users")
    ModelAndView handleProfile(HttpServletRequest req){
        HttpSession session = req.getSession();
        user = (User) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", userService.getUsers(user.getUsername()));
        return mav;
    }

    @PostMapping("users")
    ModelAndView handleProfile(@RequestParam String search){
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", userService.getUsersBy(search, user.getUsername()));
        return mav;
    }

    @RequestMapping(value="/autocomplete")
    @ResponseBody
    public List<String> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        log.info(term);
        return userService.getUsersBy(term, user.getUsername()).stream().map(el -> el.getFullname()).collect(Collectors.toList());
    }
}
