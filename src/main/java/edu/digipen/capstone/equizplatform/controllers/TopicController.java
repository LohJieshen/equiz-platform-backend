package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.entities.Topic;
import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {

    private final EQuizPlatformService quizPlatformService;

    @GetMapping("/ping")
    public String ping() {
        return "Topic Controller is online";
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Topic>> getTopicListByCourseId(@PathVariable(name="courseId") Integer courseId) {
        return ResponseEntity.ok(quizPlatformService.retrieveTopicByCourseId(courseId));
    }
}
