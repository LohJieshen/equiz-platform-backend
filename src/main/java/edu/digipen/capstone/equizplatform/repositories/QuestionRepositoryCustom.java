package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.models.QuestionAnswer;
import edu.digipen.capstone.equizplatform.models.QuestionPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepositoryCustom {
    @Query(name="Question.findAllByTopicId")
    Page<QuestionPreview> findAllByTopicId(Pageable pageable,
                                           @Param("topicId") int topicId);

    public Page<QuestionPreview> findAllDtoByCourseId(Pageable pageable, int courseId);

    public Page<QuestionPreview> findAllDto(Pageable pageable);

    List<QuestionAnswer> findAllAnswer(List<Integer> questionOrder);

    public Long countByQuizId(int quizId);
}
