package app.controller;

import app.entity.Question;
import app.entity.User;
import app.model.entity.validation.FormQuestion;
import app.security.entity.CustomUserDetails;
import app.service.QuestionService;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
public class MainController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final QuestionService questionService;

    private ModelAndView setMain(Authentication auth, List<Question> questions){
        ModelAndView mav = new ModelAndView("index");
        CustomUserDetails customUser;
        if (auth != null){
            customUser = (CustomUserDetails) auth.getPrincipal();
            mav.addObject("user", customUser.getUser());
        }
        mav.addObject("questions", questions);
        mav.addObject("question", new FormQuestion());
        return mav;
    }

    /**
     * http://localhost:8080/
     */
    @GetMapping
    ModelAndView handleMain(Authentication auth){
        return setMain(auth, questionService.getAll());
    }

    @PostMapping
    RedirectView handleQuestion(@Valid FormQuestion formQuestion, Errors errors, Authentication auth, Question question){
        if (!errors.hasErrors()){
            CustomUserDetails customUser = (CustomUserDetails) auth.getPrincipal();
            question.setDate(LocalDateTime.now().format(formatter));
            question.setUser(customUser.getUser());
            questionService.save(question);
        }
        return new RedirectView("/");
    }

    @PostMapping("/search")
    ModelAndView handleSearch(@RequestParam String search, Authentication auth){
        return setMain(auth, questionService.getAllByTitle(search));
    }

    @RequestMapping(value="/autocompletequestion")
    @ResponseBody
    public List<String> plantNamesAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        return questionService
                .getAllByTitle(term)
                .stream()
                .map(Question::getTitle)
                .distinct()
                .collect(Collectors.toList());
    }
}
