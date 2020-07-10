package app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(columnDefinition = "text")
    private String answerText;

    @NotNull
    private String date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question")
    private Question question;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "answer")
    private List<Reaction> reactions;

    public Answer(String answerText, String date, Question question, User user) {
        this.answerText = answerText;
        this.date = date;
        this.question = question;
        this.user = user;
    }
}
