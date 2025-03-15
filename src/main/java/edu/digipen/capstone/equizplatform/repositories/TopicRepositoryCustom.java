package edu.digipen.capstone.equizplatform.repositories;

import edu.digipen.capstone.equizplatform.entities.Course;
import edu.digipen.capstone.equizplatform.entities.Topic;

import java.util.List;

public interface TopicRepositoryCustom {
    List<Topic> findByCourse(Course course);
}
