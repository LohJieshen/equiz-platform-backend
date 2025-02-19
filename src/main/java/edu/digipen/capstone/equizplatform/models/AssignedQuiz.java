package edu.digipen.capstone.equizplatform.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Setter
public class AssignedQuiz {

    @NonNull
    private Integer quizId;

    @NonNull
    private Integer userId;

    @NonNull
    private String quizName;

    @NonNull
    private String courseName;

    @NonNull
    private Integer highScore;

    @NonNull
    private Integer attempts;

    @NonNull
    private Integer maxAttempts;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NonNull
    private LocalDate dueDate;

    private String status;

    private String action;
}
