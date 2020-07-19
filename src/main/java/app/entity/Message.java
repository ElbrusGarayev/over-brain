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
    private String date;

    @ManyToOne
    @JoinColumn(name = "who")
    private User who;

    @ManyToOne
    @JoinColumn(name = "whom")
    private User whom;

    public Message(String content, String date, User who, User whom) {
        this.content = content;
        this.date = date;
        this.who = who;
        this.whom = whom;
    }
}