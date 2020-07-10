package app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "who")
    private User who;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "whom")
    private User whom;

    public Follow(User who, User whom) {
        this.who = who;
        this.whom = whom;
    }
}
