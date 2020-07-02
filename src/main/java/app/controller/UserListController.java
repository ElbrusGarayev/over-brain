package app.controller;

import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class UserListController {

    private final UserService userService;

    /**
     * http://localhost:8080/users
     */
    @GetMapping("users")
    ModelAndView handleProfile(){
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", userService.getUsers());
        return mav;
    }

    @PostMapping("users")
    ModelAndView handleProfile(@RequestParam String search){
        ModelAndView mav = new ModelAndView("users");
        mav.addObject("users", userService.getUsersBy(search));
        return mav;
    }

    @RequestMapping(value="/autocomplete")
    @ResponseBody
    public List<String> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        log.info(term);
        return userService.getUsersBy(term).stream().map(el -> el.getFullname()).collect(Collectors.toList());
    }
}
