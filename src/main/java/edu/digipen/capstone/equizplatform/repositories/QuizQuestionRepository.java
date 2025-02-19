package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {
}