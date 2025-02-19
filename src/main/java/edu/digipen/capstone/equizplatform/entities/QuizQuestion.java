package edu.digipen.capstone.equizplatform.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedQueries({
        @NamedQuery(name="QuizQuestion.countByQuizId",
        query="SELECT COUNT(qq) FROM QuizQuestion qq WHERE qq.quizId = :quizId")
})

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(QuizQuestionId.class)
@Table(name="quiz_question")
public class QuizQuestion {

    @Id
    @Column(name="quiz_id")
    private Integer quizId;

    @Id
    @Column(name="question_id")
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", insertable = false, updatable = false)
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name="question_id", insertable = false, updatable = false)
    private Question question;
}
