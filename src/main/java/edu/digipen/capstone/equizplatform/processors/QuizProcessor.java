package edu.digipen.capstone.equizplatform.processors;

import edu.digipen.capstone.equizplatform.entities.*;

import edu.digipen.capstone.equizplatform.models.AssignedQuiz;
import org.springframework.boot.system.ApplicationTemp;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that has methods to process the Quiz entity and its related DTO, such as creation and editing
 */
@Component
public class QuizProcessor {

    /**
     * Checks if the current question has correct choice given as answer.
     *
     * @param question
     * @param chosenAnswerId
     * @return
     */
    public boolean isCorrectAnswer(Question question, int chosenAnswerId) {
        return question.getAnswerId() == chosenAnswerId;
    }

    /**
     * Set up a list of QuizQuestion object
     *
     * @param questionList - A list of {@code Question} objects
     * @param quiz - {@code Quiz} object
     * @return - a list of QuizQuestion object
     */
    public List<QuizQuestion> processQuizQuestions(List<Question> questionList, Quiz quiz) {

        List<QuizQuestion> quizQuestionList = new ArrayList<>();

        for (Question q : questionList) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuizId(quiz.getQuizId());
            quizQuestion.setQuiz(quiz);
            quizQuestion.setQuestionId(q.getQuestionId());
            quizQuestion.setQuestion(q);
            quizQuestionList.add(quizQuestion);
        }

        return quizQuestionList;
    }

    public List<QuizAttempt> processQuizAttempts(List<User> userList, Quiz quiz) {

        List<QuizAttempt> quizAttemptList = new ArrayList<>();

        for (User u : userList) {
            QuizAttempt quizAttempt = new QuizAttempt();
            QuizAttemptId quizAttemptId = new QuizAttemptId();
            quizAttempt.setAttempts(0);
            quizAttempt.setHighScore(0);
            quizAttemptId.setUserId(u.getUserId());
            quizAttemptId.setQuizId(quiz.getQuizId());
            quizAttempt.setId(quizAttemptId);
            quizAttempt.setUser(u);
            quizAttempt.setQuiz(quiz);
            quizAttemptList.add(quizAttempt);
        }

        return quizAttemptList;
    }

    // To check the directory for temp files
    public String testAppTemp() {
        ApplicationTemp appTemp = new ApplicationTemp();
        return appTemp.getDir().toString();
    }


    /**
     * Checks if the due date is past comparing to system date.
     *
     * @param quizDueDate
     * @return true if system date is after quiz due date, false otherwise.
     */
    public boolean isPastDueDate(LocalDate quizDueDate) {
        return LocalDate.now().isAfter(quizDueDate);
    }

}
