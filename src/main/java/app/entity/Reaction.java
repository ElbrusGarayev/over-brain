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

    @Column
    private int good;

    @Column
    private int bad;

    @OneToOne
    @JoinColumn(name = "answer")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reaction(int good, int bad, Answer answer, User user) {
        this.good = good;
        this.bad = bad;
        this.answer = answer;
        this.user = user;
    }
}
