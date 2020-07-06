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
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "who")
    private User who;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "whom")
    private User whom;
}
