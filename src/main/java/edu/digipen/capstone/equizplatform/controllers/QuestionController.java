package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.entities.Question;
import edu.digipen.capstone.equizplatform.models.QuestionDto;
import edu.digipen.capstone.equizplatform.models.QuestionPreview;
import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {

    private final EQuizPlatformService quizPlatformService;

    /**
     * Gets a QuestionDto POJO based on the given question id
     * @param questionId unique identifier for the question
     * @return {@code QuestionDto} object
     */
    @GetMapping("/{questionId}")
    public QuestionDto getQuestion(@PathVariable int questionId) {
        return quizPlatformService.getQuestion(questionId);
    }

    // Use this for api calls to get next question when doing quiz
    @GetMapping("/{userId}/{quizId}/{currQuestionNo}/next")
    public QuestionDto getNextQuestion(@PathVariable int userId,
                                       @PathVariable int quizId,
                                       @PathVariable int currQuestionNo) {
        return quizPlatformService.getNextQuestion(userId, quizId, currQuestionNo - 1);
    }

    // Use this for api calls to get prev question when doing quiz
    @GetMapping("/{userId}/{quizId}/{currQuestionNo}/prev")
    public QuestionDto getPrevQuestion(@PathVariable int userId,
                                       @PathVariable int quizId,
                                       @PathVariable int currQuestionNo) {
        return quizPlatformService.getPrevQuestion(userId, quizId, currQuestionNo - 1);
    }

    /**
     * GET Request that returns a list of questions filtered by the course the lecturer is in.
     *
     * @param page
     * @return
     */
    @GetMapping("/all")
    public Page<QuestionPreview> getAllQuestionPreview(@RequestParam(name="page", required = false) Integer page) {
        if (page == null) {
            return quizPlatformService.getAllQuestionPreview(0);
        }
        return quizPlatformService.getAllQuestionPreview(page);
    }

    @GetMapping("/all/{courseId}")
    public Page<QuestionPreview> getAllQuestionPreview(@PathVariable Integer courseId,
                                                       @RequestParam(name="page", required = false) Integer page) {
        if (page == null) {
            return quizPlatformService.getAllQuestionPreviewByCourseId(courseId, 0);
        }
        return quizPlatformService.getAllQuestionPreviewByCourseId(courseId, page);
    }

    @GetMapping("/all/topic")
    public Page<QuestionPreview> getAllQuestionPreview(@RequestParam(name="topicId") int topicId,
                                                       @RequestParam(name="page", required = false) Integer page) {
        if (page == null) {
            return quizPlatformService.getAllQuestionsByTopicId(0, topicId);
        }
        return quizPlatformService.getAllQuestionsByTopicId(page, topicId);
    }

    /**
     * Adds a new question with the following example format in Request Body:
     * <pre>
     * {@code
     *  {
     *  "questionBody": "question_text",
     *  "courseId": 1,
     *  "topicId": 1,
     *  "answerId": 1,
     *  "choices":
     *          [
     *              {
     *                  choiceId: 1,
     *                  choice_text: "Possible Answer 1"
     *              },
     *              {
     *                  choiceId: 2,
     *                  choice_text: "Possible Answer 2"
     *              },
     *              {
     *                  choiceId: 3,
     *                  choice_text: "Possible Answer 3"
     *              },
     *              {
     *                  choiceId: 4,
     *                  choice_text: "Possible Answer 4"
     *              }
     *          ]
     *
     * }
     * } </pre>
     *
     * questionId is null at this point - the repository will handle generating questionId.
     *
     * @param newQuestion - new question
     * @return ResponseEntity containing String advising if question was added successfully.
     */
    @PostMapping("/create")
    public ResponseEntity<String> addNewQuestion(@RequestBody Question newQuestion) {
        if (quizPlatformService.addNewQuestion(newQuestion)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Question Added Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Adding Question unsuccessful - Check the Request Body");
    }

    /**
     * Updates an existing question with the following example format in Request Body:
     * <pre>
     * {@code
     *  {
     *      "questionId": 1
     *      "questionBody": "question_text",
     *      "courseId": 1,
     *      "topicId": 1,
     *      "answerId": 1,
     *      "choice1": "Possible Answer 1",
     *      "choice2": "Possible Answer 2",
     *      "choice3": "Possible Answer 3",
     *      "choice4": "Possible Answer 4"
     * }
     *
     * }
     * </pre>
     * @param updatedQuestion - updated question
     * @return true if successfully added, false otherwise.
     */
    @PutMapping("/update")
    public boolean updateQuestion(@RequestBody Question updatedQuestion) {
        return quizPlatformService.updateQuestion(updatedQuestion);
    }

    @PutMapping("/answer")
    public boolean answerCurrQuestion(@RequestParam Integer userId,
                                      @RequestParam Integer quizId,
                                      @RequestParam Integer questionNo,
                                      @RequestParam Integer choice) {
        return quizPlatformService.updateSelectedChoice(userId, quizId, questionNo, choice);
    }
}
