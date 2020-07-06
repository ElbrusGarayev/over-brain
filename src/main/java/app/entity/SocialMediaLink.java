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
public class SocialMediaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String facebook;
    private String twitter;
    private String instagram;
    private String github;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
