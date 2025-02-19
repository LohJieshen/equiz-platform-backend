package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourseRepositoryCustomImpl implements CourseRepositoryCustom{

    private EntityManager entityManager;
    @Override
    public Course findByQuizId(int quizId) {
        TypedQuery<Course> query = entityManager.createNamedQuery("Course.findByQuizId", Course.class);
        query.setParameter("quizId", quizId);
        return query.getSingleResult();
    }
}
