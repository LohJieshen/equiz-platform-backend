package edu.digipen.capstone.equizplatform.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NamedQueries({
        @NamedQuery(name="Quiz.findAllByUserId",
        query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuizPreview(" +
                "qz.quizId, qz.quizName, c.courseName) " +
                "FROM Quiz qz JOIN Course c ON qz.courseId = c.courseId " +
                "JOIN QuizAttempt qa ON qz.quizId = qa.id.quizId " +
                "WHERE qa.id.userId = :userId"),
        @NamedQuery(name="Quiz.findAllByCourseId",
                query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuizPreview(" +
                        "qz.quizId, qz.quizName, c.courseName) " +
                        "FROM Quiz qz JOIN Course c ON qz.courseId = c.courseId " +
                        "WHERE c.courseId = :courseId"),
        @NamedQuery(name="Quiz.findAssignedQuizByUserId",
                query="SELECT NEW edu.digipen.capstone.equizplatform.models.AssignedQuiz(" +
                        "qa.id.quizId, qa.id.userId, qz.quizName, c.courseName, qa.highScore, qa.attempts, " +
                        "qz.maxAttempts, qz.dueDate) FROM QuizAttempt qa " +
                        "JOIN Quiz qz ON qa.id.quizId = qz.quizId " +
                        "JOIN Course c ON qz.courseId = c.courseId " +
                        "WHERE qa.id.userId = :userId"),
        @NamedQuery(name="Quiz.findQuizStatusByCourseId",
        query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuizStatus(" +
                "u.userId, u.firstName, u.lastName, qz.quizId, qz.quizName, qa.attempts, qa" +
                ".highScore) FROM Quiz qz " +
                "JOIN QuizAttempt qa ON qz.quizId = qa.id.quizId " +
                "JOIN User u ON qa.id.userId = u.userId " +
                "WHERE u.course.courseId = :courseId")
})
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_id")
    private Integer quizId;

    @Column(name="quiz_name")
    private String quizName;

    @Column(name="course_id")
    private Integer courseId;

    @Column(name="max_attempts")
    private int maxAttempts;

    @Column(name="is_removed")
    private boolean isRemoved;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="due_date")
    private LocalDate dueDate; // last day a user can start quiz

    @OneToMany(mappedBy="quiz")
    private List<QuizAttempt> quizAttempts;

    @OneToMany(mappedBy="quiz")
    private List<QuizQuestion> questionList;
}
