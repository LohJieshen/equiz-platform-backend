package edu.digipen.capstone.equizplatform.models;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link edu.digipen.capstone.equizplatform.entities.Question}
 */
@Value
public class QuestionAnswer implements Serializable {

    Integer questionId;

    int answerId;
}