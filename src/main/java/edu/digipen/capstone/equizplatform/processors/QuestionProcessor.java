package edu.digipen.capstone.equizplatform.processors;

import edu.digipen.capstone.equizplatform.entities.Question;
import edu.digipen.capstone.equizplatform.models.QuestionDto;
import org.springframework.stereotype.Component;

/**
 * The QuestionProcessor class contains method that does general conversion between Question entity and
 * its DTO, as well as methods for processing fields in such objects.
 */
@Component
public class QuestionProcessor {

    public QuestionDto convertDto(Question question) {
        String questionBody = question.getQuestionBody();
        String choice1 = question.getChoice1();
        String choice2 = question.getChoice2();
        String choice3 = question.getChoice3();
        String choice4 = question.getChoice4();

        return new QuestionDto(questionBody, choice1, choice2, choice3, choice4);
    }

    /**
     * Truncates the text in a string down to 150 characters and appends "..." to the end
     * if it's over 150 characters. Otherwise returns the string as-is.
     *
     * @param questionBody
     * @return String
     */
    public String truncateQuestionBody(String questionBody) {
        return questionBody.length() > 150 ?
                questionBody.substring(0, 150) + "..." :
                questionBody;
    }

    /**
     * Checks if the question body is valid.
     *
     * @param questionBody
     * @return
     */
    public boolean isValidQuestionBody(String questionBody) {
        return !questionBody.isBlank();
    }
}
