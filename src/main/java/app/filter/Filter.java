package app.filter;

import app.entity.Answer;
import app.entity.Reaction;
import app.entity.User;
import app.model.entity.AnswerView;
import app.model.entity.UserView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Filter {

    public List<AnswerView> answerConverter(List<Answer> answers){
        return answers.stream().map(answer ->
                new AnswerView(answer.getId(), answer.getAnswerText(), answer.getDate(),
                        answer.getQuestion(), answer.getUser(),
                        answer.getReactions().stream().filter(el -> el.isStatus()).count(),
                        answer.getReactions().stream().filter(el -> !el.isStatus()).count())
        ).collect(Collectors.toList());
    }
    public List<UserView> userConverter(Page<User> users){
        return users.stream().map(user ->
                new UserView(
                        user.getFullname(), user.getUsername(), user.getCountry(), user.getPhoto(),
                        getGoodReactions(user.getAnswers())
                ))
                .sorted(Comparator.comparingLong(UserView::getReactions).reversed())
                .collect(Collectors.toList());
    }

    private long getGoodReactions(List<Answer> answers){
        return answers.stream()
                .mapToLong(answer ->
                        answer.getReactions().stream()
                                .filter(Reaction::isStatus).count()).sum();
    }
}
