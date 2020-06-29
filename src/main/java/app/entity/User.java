package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String fullname;

    @NotNull
    private String country;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private byte[] photo;

    private String lastseen;

    @OneToOne(mappedBy = "user")
    private SocialMediaLink mediaLink;

    @OneToMany(mappedBy = "user")
    private List<Question> question;

    @OneToMany(mappedBy = "who")
    private List<Reaction> reactionsFromMe;

    @OneToMany(mappedBy = "whom")
    private List<Reaction> reactionsToMe;

    @OneToMany(mappedBy = "who")
    private List<Message> messagesFromMe;

    @OneToMany(mappedBy = "whom")
    private List<Message> messagesToMe;

    @OneToMany(mappedBy = "who")
    private List<Follow> followers;

    @OneToMany(mappedBy = "whom")
    private List<Follow> followings;
}
