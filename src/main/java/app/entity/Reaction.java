package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "answer")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reaction(boolean status, Answer answer, User user) {
        this.status = status;
        this.answer = answer;
        this.user = user;
    }
}
