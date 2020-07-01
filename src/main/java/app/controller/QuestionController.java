package app.controller;

import app.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * http://localhost:8080/question
     */
    @GetMapping("{id}")
    ModelAndView handleQuestion(Model model, @PathVariable long id){
        ModelAndView mav = new ModelAndView("question");
        mav.addObject("question", questionService.getQuestionById(id).get());
        return mav;
    }
}
