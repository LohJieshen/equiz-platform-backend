package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.models.QuestionAnswer;
import edu.digipen.capstone.equizplatform.models.QuestionPreview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@AllArgsConstructor
@Repository
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<QuestionPreview> findAllByTopicId(Pageable pageable, int topicId) {
        TypedQuery<QuestionPreview> query = entityManager.createNamedQuery("Question.findAllByTopicId",
                QuestionPreview.class);
        query.setParameter("topicId", topicId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public Page<QuestionPreview> findAllDto(Pageable pageable) {
        TypedQuery<QuestionPreview> query = entityManager.createNamedQuery("Question.findAllDto",
                QuestionPreview.class);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public Page<QuestionPreview> findAllDtoByCourseId(Pageable pageable, int courseId) {
        TypedQuery<QuestionPreview> query = entityManager.createNamedQuery("Question.findAllByCourseId",
                QuestionPreview.class);
        query.setParameter("courseId", courseId);
        return new PageImpl<>(query.getResultList(), pageable, query.getResultList().size());
    }

    @Override
    public List<QuestionAnswer> findAllAnswer(List<Integer> questionIdList) {
        TypedQuery<QuestionAnswer> query = entityManager.createNamedQuery("Question.findAllAnswers",
                QuestionAnswer.class);
        query.setParameter("questionIdList", questionIdList);
        return query.getResultList();
    }

    @Override
    public Long countByQuizId(int quizId) {
        TypedQuery<Long> query = entityManager.createNamedQuery("QuizQuestion.countByQuizId",
                Long.class);
        query.setParameter("quizId", quizId);
        return query.getSingleResult();
    }
}

