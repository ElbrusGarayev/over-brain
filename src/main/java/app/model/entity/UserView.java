package app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserView {

    private String fullname;

    private String username;

    private String country;

    private String photo;

    private long reactions;

}
