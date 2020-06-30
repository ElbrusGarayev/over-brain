package app.controller;

import app.entity.Question;
import app.entity.User;
import app.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/main")
public class MainController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final QuestionService questionService;

    /**
     * http://localhost:8080/main
     */
    @GetMapping("#")
    String handleMain2(Model model){
        model.addAttribute("questions", questionService.getAll());
        return "index";
    }

    @GetMapping
    String handleMain(Model model){
        model.addAttribute("questions", questionService.getAll());
        return "index";
    }

    @PostMapping
    String handleQuestion(Question question, HttpServletRequest req){
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        question.setDate(LocalDateTime.now().format(formatter));
        question.setUser(user);
        questionService.save(question);
        return "index";
    }
}
