package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.Course;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepositoryCustom {
    Course findByQuizId(int quizId);
}
