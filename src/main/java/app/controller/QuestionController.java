package app.controller;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import app.filter.Filter;
import app.security.entity.CustomUserDetails;
import app.service.AnswerService;
import app.service.QuestionService;
import app.service.ReactionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ReactionService reactionService;
    private final Filter filter;

    private static long questionId;
    private static User currUser;

    /**
     * http://localhost:8080/question/1
     */
    @GetMapping("{id}")
    ModelAndView handleQuestion(@PathVariable long id, Authentication auth) {
        CustomUserDetails customUser = (CustomUserDetails) auth.getPrincipal();
        currUser = customUser.getUser();
        questionId = id;
        ModelAndView mav = new ModelAndView("question");
        mav.addObject("question", questionService.getQuestionById(id).get());
        mav.addObject("answers", filter.entityConverter(answerService.getAllAnswerByQId(id)));
        return mav;
    }

    @PostMapping("{id}")
    RedirectView handleAnswer(@RequestParam Optional<String> answerText, @PathVariable long id) {
        answerService.save(new Answer(answerText.get(), LocalDateTime.now().format(formatter),
                questionService.getQuestionById(id).get(), currUser));
        return new RedirectView("/question/" + id);
    }

    @PostMapping("/react")
    RedirectView handleLike(HttpServletRequest req) {
        long answerId = Long.parseLong(req.getParameter("answerId"));
        String button = req.getParameter("button");
        Answer currAnswer = answerService.getAnswerById(answerId).get();
        Optional<Reaction> reaction = reactionService.getByAnswerIdAndUser(currAnswer, currUser);
        if (button.equals("like")) {
            if (reaction.isPresent()) {
                reaction.get().setStatus(true);
                reactionService.save(reaction.get());
            } else
                reactionService.save(new Reaction(true, currAnswer, currUser));
        } else {
            if (reaction.isPresent()) {
                reaction.get().setStatus(false);
                reactionService.save(reaction.get());
            } else
                reactionService.save(new Reaction(false, currAnswer, currUser));
        }
        return new RedirectView("/question/" + questionId);
    }
}
