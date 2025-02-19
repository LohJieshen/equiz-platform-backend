package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.User;
import edu.digipen.capstone.equizplatform.models.UserBasicProfileInfo;
import edu.digipen.capstone.equizplatform.models.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface that contains custom methods
 */
public interface UserRepositoryCustom {

    public List<User> findAllByCourseId(Integer courseId);
}
