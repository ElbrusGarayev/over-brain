package app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
public class SocialMediaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String facebook;
    private String linkedin;
    private String instagram;
    private String github;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
