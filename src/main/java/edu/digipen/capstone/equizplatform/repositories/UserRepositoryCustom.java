package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.User;

import java.util.List;

/**
 * Repository interface that contains custom methods
 */
public interface UserRepositoryCustom {

    List<User> findAllByCourseId(Integer courseId);
}
