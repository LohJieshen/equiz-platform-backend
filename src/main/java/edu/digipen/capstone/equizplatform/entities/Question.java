package edu.digipen.capstone.equizplatform.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NamedQueries({
        @NamedQuery(name="Question.findAllByCourseId",
        query="SELECT DISTINCT NEW edu.digipen.capstone.equizplatform.models.QuestionPreview(" +
                "qn.questionId, c.courseName, t.topicName, qn.questionBody) " +
                "FROM Question qn " +
                "JOIN QuizQuestion qq ON qn.questionId = qq.id.questionId " +
                "JOIN QuizAttempt qa ON qq.quizId = qa.id.quizId " +
                "JOIN User u ON qa.id.userId = u.userId " +
                "JOIN Topic t ON qn.topicId = t.topicId " +
                "JOIN Course c ON u.course.courseId = c.courseId " +
                "WHERE c.courseId = :courseId"),
/*        @NamedQuery(name="Question.findAllByCourseId",
                query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuestionPreview(" +
                        "qn.questionId, c.courseName, t.topicName, qn.questionBody) " +
                        "FROM Question qn JOIN qn.topic t ON qn.topicId = t.topicId " +
                        "JOIN Course c ON t.course.courseId = c.courseId " +
                        "WHERE c.courseId = :courseId"),*/
        @NamedQuery(name="Question.findAllByTopicId",
                query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuestionPreview(" +
                        "qn.questionId, c.courseName, t.topicName, qn.questionBody) " +
                        "FROM Question qn JOIN qn.topic t " +
                        "JOIN t.course c " +
                        "WHERE t.topicId = :topicId"),
        @NamedQuery(name="Question.findQuestionIdByQuizId",
        query="SELECT qn.questionId FROM Question qn JOIN qn.quizQuestion qz WHERE qz.quizId = :quizId"),
        @NamedQuery(name="Question.findAllAnswers",
        query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuestionAnswer(" +
                "qn.questionId, qn.answerId) FROM Question qn " +
                "WHERE qn.answerId IN :questionIdList"),
        @NamedQuery(name="Question.findAllDto",
        query="SELECT NEW edu.digipen.capstone.equizplatform.models.QuestionPreview(" +
                "qn.questionId, c.courseName, t.topicName, qn.questionBody) " +
                "FROM Question qn JOIN qn.topic t " +
                "JOIN t.course c"),
})
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private Integer questionId;

    @Column(name="question_body")
    private String questionBody;

    @Column(name="topic_id")
    private Integer topicId;

    @Column(name="ans_id")
    private int answerId;

    @Column(name="is_removed")
    private boolean isRemoved;

    @Column(name="choice_1")
    private String choice1;

    @Column(name="choice_2")
    private String choice2;

    @Column(name="choice_3")
    private String choice3;

    @Column(name="choice_4")
    private String choice4;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="topic_id", insertable=false, updatable=false)
    private Topic topic;

    @OneToMany(mappedBy = "question")
    private List<QuizQuestion> quizQuestion;


}
