package edu.digipen.capstone.equizplatform.models;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link edu.digipen.capstone.equizplatform.entities.Question}
 */
@Value
public class QuestionDto implements Serializable {

    String questionBody;

    String choice1;

    String choice2;

    String choice3;

    String choice4;
}