package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
public class SocialMediaLink {
    @Id
    private long id;

    private String facebook;
    private String twitter;
    private String instagram;
    private String github;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
}
