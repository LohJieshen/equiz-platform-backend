package edu.digipen.capstone.equizplatform.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A class that defines the fields for each object representing the preview of each question
 */
@AllArgsConstructor
@Getter
@Setter
public class QuestionPreview {

    private int questionId;

    private String courseName;

    private String topicName;

    private String questionBody;
}
