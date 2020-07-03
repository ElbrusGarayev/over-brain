package app.filter;

import app.entity.Answer;
import app.model.entity.AnswerView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Filter {

    public List<AnswerView> entityConverter(List<Answer> answers){
        return answers.stream().map(answer ->
                new AnswerView(answer.getId(), answer.getAnswerText(), answer.getDate(),
                        answer.getQuestion(), answer.getUser(),
                        answer.getReactions().stream().filter(el -> el.isStatus() == true).count(),
                        answer.getReactions().stream().filter(el -> el.isStatus() == false).count())
        ).collect(Collectors.toList());
    }
}
