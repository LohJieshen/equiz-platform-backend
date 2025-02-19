package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer>, QuizRepositoryCustom {
}
