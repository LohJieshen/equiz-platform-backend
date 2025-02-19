package edu.digipen.capstone.equizplatform.entities;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class QuizQuestionId implements Serializable {

    private Integer quizId;

    private Integer questionId;

}
