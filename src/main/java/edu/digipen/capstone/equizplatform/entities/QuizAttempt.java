package edu.digipen.capstone.equizplatform.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Entity class that represents each quizAttempt entry in quiz_attempt table.
 */

@Getter
@Setter
@Entity
@Table(name = "quiz_attempt", schema = "equiz")
public class QuizAttempt {
    @EmbeddedId
    private QuizAttemptId id;

    @MapsId("quizId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "high_score")
    private Integer highScore;

    @Column(name = "attempts")
    private Integer attempts;

}