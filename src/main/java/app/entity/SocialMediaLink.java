package app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
}
