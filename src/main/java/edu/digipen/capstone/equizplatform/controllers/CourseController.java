package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.entities.Course;
import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final EQuizPlatformService quizPlatformService;

    @GetMapping("/getName")
    public String getCourseNameByQuizId(@RequestParam Integer quizId) {
        return quizPlatformService.getCourseNameByQuizId(quizId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Course> getCourseByUserId(Integer userId) {
        return ResponseEntity.ok(quizPlatformService.retrieveCourse(userId));
    }
}
