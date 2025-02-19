package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.models.AssignedQuiz;
import edu.digipen.capstone.equizplatform.models.QuizPreview;
import edu.digipen.capstone.equizplatform.models.QuizStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface QuizRepositoryCustom {

    public Page<QuizPreview> findAllByUserId(int userId, Pageable pageable);

    public Page<QuizPreview> findAllByCourseId(int courseId, Pageable pageable);

    public Page<AssignedQuiz> findAllAssignedQuizByUserId(int userId, Pageable pageable);

    public Page<QuizStatus> findAllQuizStatusByUserId(int courseId, Pageable pageable);
}
