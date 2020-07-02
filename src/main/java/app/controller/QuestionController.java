package app.controller;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import app.service.AnswerService;
import app.service.QuestionService;
import app.service.ReactionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ReactionService reactionService;

    /**
     * http://localhost:8080/question/1
     */
    @GetMapping("{id}")
    ModelAndView handleQuestion(@PathVariable long id, HttpServletRequest req){
        ModelAndView mav = new ModelAndView("question");
        mav.addObject("question", questionService.getQuestionById(id).get());
        mav.addObject("answers", answerService.getAllAnswerByQId(id));

//        log.info(reactionService.getReactionsByUIdAndAnswerId(1, 1));

        return mav;
    }

    @PostMapping("{id}")
    RedirectView handleQuestion(Answer answer, @PathVariable long id, HttpServletRequest req){
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String button = req.getParameter("button");
        if(button.equals("Submit Answer")){
            answer.setId(answerService.getAnswersCount() + 1);
            answer.setQuestion(questionService.getQuestionById(id).get());
            answer.setUser(user);
            answer.setDate(LocalDateTime.now().format(formatter));
            answerService.save(answer);
        }
        if (button.equals("like")){
            long answerId = Long.parseLong(req.getParameter("answerId"));
            reactionService.save(new Reaction(true, answerService.getAnswerById(answerId).get(), user));
//            Reaction reaction = reactionService.getByAnswerId(answerId);
//            if(reaction != null){
//                reaction.setGood(reaction.getGood() + 1);
//                reactionService.save(reaction);
//            }else {
//                Answer answer1 = answerService.getAnswerById(answerId).get();
//                reactionService.save(new Reaction(1, 0, answer1, user));
//            }
        }
        if (button.equals("dislike")){
            long answerId = Long.parseLong(req.getParameter("answerId"));
            reactionService.save(new Reaction(false, answerService.getAnswerById(answerId).get(), user));
//            Reaction reaction = reactionService.getByAnswerId(answerId);
//            if(reaction != null){
//                reaction.setBad(reaction.getBad() + 1);
//                reactionService.save(reaction);
//            }else {
//                Answer answer1 = answerService.getAnswerById(answerId).get();
//                reactionService.save(new Reaction(0, 1, answer1, user));
//            }
        }
        return new RedirectView("/question/" + id);
    }
}
