package app.controller;

import app.entity.User;
import app.filter.Filter;
import app.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/users")
public class UserListController {

    private final UserService userService;
    private final Filter filter;

    private ModelAndView getModelAndView(Optional<Integer> page, Optional<String> search) {
        ModelAndView mav = new ModelAndView("users");
        page = Optional.of(page.orElse(0));
        Page<User> currPage = userService.getAll(search.orElse(""), page);
        mav.addObject("users", filter.userConverter(currPage));
        mav.addObject("totalPages", currPage.getTotalPages());
        mav.addObject("page", page.get());
        mav.addObject("search", search.orElse(""));
        return mav;
    }

    /**
     * http://localhost:8080/users
     */
    @GetMapping
    ModelAndView handleUsers(@RequestParam Optional<Integer> page, @RequestParam Optional<String> search){
        return getModelAndView(page, search);
    }

    @PostMapping
    ModelAndView handleSearch(@RequestParam Optional<Integer> page, @RequestParam Optional<String> search){
        return getModelAndView(page, search);
    }

    @RequestMapping(value="/autocomplete")
    @ResponseBody
    public List<String> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        return userService
                .getUsersBy(term)
                .stream()
                .map(User::getFullname)
                .distinct()
                .collect(Collectors.toList());
    }
}
