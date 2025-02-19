package edu.digipen.capstone.equizplatform.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NamedQueries({
        @NamedQuery(name="Course.findByQuizId",
        query="SELECT DISTINCT c FROM Quiz qz JOIN Course c ON qz.courseId = c.courseId WHERE qz.quizId = :quizId")
})
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="course_id")
    private Integer courseId;

    @Column(name="course_name")
    private String courseName;

    @OneToMany(mappedBy="course")
    private List<User> userList;

    @OneToMany(mappedBy="course", cascade = CascadeType.ALL)
    private List<Topic> topicList;

    @OneToMany
    @JoinColumn(name="course_id",
            referencedColumnName="course_id")
    private List<Quiz> quizList;
}
