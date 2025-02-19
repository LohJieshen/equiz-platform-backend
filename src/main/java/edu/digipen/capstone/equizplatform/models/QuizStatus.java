package edu.digipen.capstone.equizplatform.models;

import lombok.*;

/**
 * Model POJO that represents the status for each quiz for a given student.
 */
@Getter
@Setter
@AllArgsConstructor
public class QuizStatus {

    private Integer userId;

    private String firstName;

    private String lastName;

    private Integer quizId;

    private String quizName;

    private int attempts;

    private int highScore;
}
