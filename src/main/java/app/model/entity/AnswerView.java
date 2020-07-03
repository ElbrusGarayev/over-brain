package app.model.entity;

import app.entity.Question;
import app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerView {
    private long id;

    private String answerText;

    private String date;

    private Question question;

    private User user;

    private long goodReactions;

    private long badReactions;
}
