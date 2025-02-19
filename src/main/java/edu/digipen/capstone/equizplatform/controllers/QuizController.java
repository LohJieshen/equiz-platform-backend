package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.models.*;
import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for methods which returns Quiz-related objects or a list/set of Quiz objects.
 */
@RestController
@RequestMapping("/quiz")
@AllArgsConstructor
public class QuizController {

    private final EQuizPlatformService quizPlatformService;

    @GetMapping("/test-app-temp")
    public String testAppTemp() {
        return quizPlatformService.testAppTemp();
    }

    @GetMapping("/assigned/{userId}")
    public Page<AssignedQuiz> getAssignedQuizList(@PathVariable int userId,
                                                  @RequestParam(required=false) Integer page) {
        if (page == null) {
            return quizPlatformService.getAssignedQuizByUserId(userId, 0);
        }
        return quizPlatformService.getAssignedQuizByUserId(userId, page);
    }

    @GetMapping("/course/{courseId}")
    public Page<QuizPreview> getQuizListByCourseId(@PathVariable int courseId,
                                                   @RequestParam(required=false) Integer page) {
        if (page == null){
            return quizPlatformService.getQuizListByCourseId(courseId, 0);
        }
        return quizPlatformService.getQuizListByCourseId(courseId, page);
    }

    /**
     * <p/>Takes in a POST request containing JSON response with a structure representing a new quiz and creates a new
     * quiz.
     *
     * <p><pre>
     *
     *     {@code
     *     {
     *         "quizName": "Test quiz name",
     *         "courseId": 1,
     *         "maxAttempts": 10,
     *         "dueDate": "2024-12-31",
     *         "selectedQuestionIdList": [
     *              1,
     *              2,
     *              3,
     *              4,
     *              5
     *         ]
     *      }
     *     }
     * </p></pre>
     * @param newQuiz
     * @return {@code Integer} quizId for the newly created quiz
     */
    @PostMapping("/create")
    public Integer addQuiz(@RequestBody QuizCreation newQuiz) {
        return quizPlatformService.addNewQuiz(newQuiz);
    }

    /**
     * Launches the quiz
     * @param quizId
     * @param userId
     * @return
     */
    @GetMapping("/start/{quizId}")
    public CurrQuizAttempt launchQuiz(@PathVariable int quizId,
                             @RequestParam int userId) {
        return quizPlatformService.launchQuiz(userId, quizId);
    }

    /**
     * Takes in a JSON body for {@code CurrQuizAttempt}, ends the quiz, and returns {@code QuizResult} to the caller.
     *
     * <p> The structure of the JSON body:
     *     <pre>
     *      {@code
     *      {
     *          "quizId" : 1,
     *          "userId" : 230001,
     *          "quizName" : "Data Structure and Algorithms in Java",
     *          "courseName" : "Software Engineering",
     *          "currQuestion" : 3,
     *          "questionOrder": [3,1,2],
     *          "selectedChoices": [1,2,1]
     *      }
     *
     *      } </p></pre>
     *
     * @param currQuizAttempt
     * @return
     */
    @PutMapping("/end/{quizId}")
    public QuizResult getQuizResult(@RequestBody CurrQuizAttempt currQuizAttempt) {
        return quizPlatformService.endQuiz(currQuizAttempt);
    }

    @GetMapping("/status/{courseId}")
    public Page<QuizStatus> getAllQuizStatus(@PathVariable int courseId,
                                            @RequestParam(required=false) Integer page) {
        if (page == null) {
            return quizPlatformService.getQuizStatusByCourseId(courseId, 0);
        }
        return quizPlatformService.getQuizStatusByCourseId(courseId, page);
    }
}