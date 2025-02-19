package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.QuizAttempt;
import edu.digipen.capstone.equizplatform.entities.QuizAttemptId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, QuizAttemptId> {
}