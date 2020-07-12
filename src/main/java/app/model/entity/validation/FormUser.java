package app.model.entity.validation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class FormUser {
    @NotEmpty(message = "Fullname field should not be empty")
    private String fullname;

    @NotEmpty(message = "Country field should not be empty")
    private String country;

    @NotEmpty(message = "Username field should not be empty")
    @Pattern(regexp = "\\b[a-zA-Z][a-zA-Z0-9\\-._]{3,}\\b", message = "Wrong username")
    private String username;

    @NotEmpty(message = "Email field should not be empty")
    @Email(regexp = "^(.+)@(.+)$", message = "Invalid email pattern")
    private String email;

    @Size(min = 8, message = "Password length shouldn't be less than 8")
    private String password;
}
