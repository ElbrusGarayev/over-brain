package app.controller;

import app.entity.Question;
import app.entity.User;
import app.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final QuestionService questionService;

    /**
     * http://localhost:8080/main
     */
    @GetMapping("main")
    ModelAndView handleMain(HttpServletRequest req){
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", user);
        mav.addObject("questions", questionService.getAll());
        return mav;
    }

    @PostMapping("main")
    RedirectView handleQuestion(Question question, HttpServletRequest req){
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        question.setDate(LocalDateTime.now().format(formatter));
        question.setUser(user);
        questionService.save(question);
        return new RedirectView("/main");
    }

    @PostMapping("/search")
    ModelAndView handleSearch(@RequestParam String search){
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("questions", questionService.getAllByTitle(search));
        return mav;
    }

    @RequestMapping(value="/autocompletequestion")
    @ResponseBody
    public List<String> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        return questionService.getAllByTitle(term).stream().map(question -> question.getTitle()).collect(Collectors.toList());
    }
}
