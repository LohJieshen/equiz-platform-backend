package edu.digipen.capstone.equizplatform.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class that acts as a Data Transfer Object to show basic Quiz details, typically as part of a list.
 */

@Getter
@Setter
@AllArgsConstructor
public class QuizPreview {

    private Integer quizId;

    private String quizName;

    private String courseName;
}
