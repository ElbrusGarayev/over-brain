package app.model.entity.validation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class FormQuestion {

    @Size(min = 3, message = "Title length shouldn't be less than 3")
    private String title;

    @Size(min = 10, message = "Details length shouldn't be less than 10")
    @Column(columnDefinition="text")
    private String details;
}
