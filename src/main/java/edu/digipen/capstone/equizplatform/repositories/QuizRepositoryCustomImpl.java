package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.models.AssignedQuiz;
import edu.digipen.capstone.equizplatform.models.QuizPreview;
import edu.digipen.capstone.equizplatform.models.QuizStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<QuizPreview> findAllByUserId(int userId, Pageable pageable) {
        TypedQuery<QuizPreview> query = entityManager.createNamedQuery("Quiz.findAllByUserId",
                QuizPreview.class);
        query.setParameter("userId", userId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public Page<QuizPreview> findAllByCourseId(int courseId, Pageable pageable) {
        TypedQuery<QuizPreview> query = entityManager.createNamedQuery("Quiz.findAllByCourseId",
                QuizPreview.class);
        query.setParameter("courseId", courseId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public Page<AssignedQuiz> findAllAssignedQuizByUserId(int userId, Pageable pageable) {
        TypedQuery<AssignedQuiz> query = entityManager.createNamedQuery("Quiz.findAssignedQuizByUserId",
                AssignedQuiz.class);
        query.setParameter("userId", userId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public Page<QuizStatus> findAllQuizStatusByUserId(int courseId, Pageable pageable) {
        TypedQuery<QuizStatus> query = entityManager.createNamedQuery("Quiz.findQuizStatusByCourseId",
                QuizStatus.class);
        query.setParameter("courseId", courseId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }
}
