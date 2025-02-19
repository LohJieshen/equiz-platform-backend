package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {

    private final EQuizPlatformService quizPlatformService;

    @GetMapping("/ping")
    public String ping() {
        return "Topic Controller is online";
    }
}
