package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllByCourseId(Integer courseId) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findAllByCourseId", User.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }
}