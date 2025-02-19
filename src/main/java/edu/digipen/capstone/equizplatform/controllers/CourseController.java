package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private final EQuizPlatformService quizPlatformService;

    @GetMapping("/getName")
    public String getCourseNameByQuizId(@RequestParam Integer quizId) {
        return quizPlatformService.getCourseNameByQuizId(quizId);
    }
}
