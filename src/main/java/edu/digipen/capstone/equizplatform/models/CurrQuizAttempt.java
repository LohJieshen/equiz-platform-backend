package edu.digipen.capstone.equizplatform.models;
import lombok.Data;

import java.util.List;

/**
 * POJO that is used as a data structure to store the current Quiz attempt.
 */
@Data
public class CurrQuizAttempt {

    private Integer quizId;

    private Integer userId;

    private String quizName;

    private String courseName;

    private int currQuestion;

    private List<Integer> questionOrder;

    private List<Integer> selectedChoices;
}
