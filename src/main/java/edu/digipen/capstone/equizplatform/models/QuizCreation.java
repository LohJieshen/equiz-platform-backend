package edu.digipen.capstone.equizplatform.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link edu.digipen.capstone.equizplatform.entities.Quiz}
 */
@AllArgsConstructor
@Getter
@ToString
public class QuizCreation {

    @NotNull
    @Size(max=50)
    @NotEmpty
    @NotBlank
    private final String quizName;

    @NotNull
    @Positive
    private final Integer courseId;

    @Positive
    private final int maxAttempts;

    private final boolean isRemoved;

    @NotNull
    @Future
    private final LocalDate dueDate;

    private List<Integer> selectedQuestionIdList;
}