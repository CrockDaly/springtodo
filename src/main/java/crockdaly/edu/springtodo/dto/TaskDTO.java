package crockdaly.edu.springtodo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import crockdaly.edu.springtodo.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;

    @NotNull(message = "Title cannot be null")
    private String title;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy")
    private LocalDate dueDate;

    private TaskStatus status;
}

