package edu.digipen.capstone.equizplatform.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*@NamedQueries({
        @NamedQuery(name="getTopicNamesInCourse",
        query="SELECT t.id, t.topicName FROM Topic t WHERE t.course.courseId = :courseId")
})*/
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="topic_id")
    private Integer topicId;

    @Column(name="topic_name")
    private String topicName;

    // TODO - If I introduce courseId as a field, will fail to compile. Investigate.
/*
    @Column(name="course_id")
    private Integer courseId;
*/


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @JsonIgnore
    @OneToOne(mappedBy="topic")
    private Question question;
}
