package edu.digipen.capstone.equizplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class QuizAttemptId implements Serializable {

    private static final long serialVersionUID = 986811916000954013L;
    @NotNull
    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuizAttemptId entity = (QuizAttemptId) o;
        return Objects.equals(this.quizId, entity.quizId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, userId);
    }

}