package app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private boolean status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "answer")
    private Answer answer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reaction(boolean status, Answer answer, User user) {
        this.status = status;
        this.answer = answer;
        this.user = user;
    }
}
