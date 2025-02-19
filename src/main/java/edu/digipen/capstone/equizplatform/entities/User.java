package edu.digipen.capstone.equizplatform.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NamedQueries({
        @NamedQuery(name="User.findBasicUserProfileInfo",
        query="SELECT new edu.digipen.capstone.equizplatform.models.UserBasicProfileInfo(" +
                "u.firstName, u.course.courseId, u.isLecturer, u.lastLoginDate) " +
                "FROM User u WHERE u.userId = :id"),
        @NamedQuery(name="User.findUserCredentials",
        query="SELECT new edu.digipen.capstone.equizplatform.models.UserCredentials(" +
                "u.userId, u.password, u.isLecturer) " +
                "FROM User u WHERE u.userId = :userId AND u.password = :password "),
        @NamedQuery(name="User.findAllByCourseId",
        query="SELECT u FROM User u WHERE u.course.courseId = :courseId")
})

@NoArgsConstructor
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="password")
    private String password;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name="last_login_date")
    private LocalDate lastLoginDate;

    @Column(name="is_lecturer")
    private boolean isLecturer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="course_id")
    private Course course;
}
