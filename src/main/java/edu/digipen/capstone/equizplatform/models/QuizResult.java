package edu.digipen.capstone.equizplatform.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO that contains the result and various information for a given completed quiz.
 */
@Data
@AllArgsConstructor
public class QuizResult {

    private int quizId;

    private String quizName;

    private int score;

    private int totalQuestions;

    private List<Boolean> results;
}
