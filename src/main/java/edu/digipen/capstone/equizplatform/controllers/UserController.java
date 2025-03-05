package edu.digipen.capstone.equizplatform.controllers;

import edu.digipen.capstone.equizplatform.entities.User;
import edu.digipen.capstone.equizplatform.models.UserBasicProfileInfo;
import edu.digipen.capstone.equizplatform.models.UserCredentials;
import edu.digipen.capstone.equizplatform.services.EQuizPlatformService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final EQuizPlatformService eQuizPlatformService;

    @GetMapping("/basic-info/{userId}")
    public UserBasicProfileInfo getUserBasicInfo(@PathVariable int userId) {
        return eQuizPlatformService.getUserBasicInfo(userId);
    }

    /**
     * Endpoint that takes in a JSON response with the structure given below:
     * <pre>{@code
     *
     * {
     *     "userId": 230001,
     *     "password": "tenaciousD"
     * }
     * }</pre>
     * @param userCredentials user credentials DTO object
     * @return 202 and true if successful, 401 and false otherwise.
     */
    @PutMapping("/login")
    public ResponseEntity<Boolean> authenticateUserLoginCredentials(@RequestBody UserCredentials userCredentials) {

        if (eQuizPlatformService.authenticateUserLogin(userCredentials)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/check-lecturer/{userId}")
    public ResponseEntity<Boolean> checkLecturerAccess(@PathVariable int userId) {
        if (eQuizPlatformService.checkLecturerAccess(userId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @PutMapping("/update-last-login/{userId}")
    public ResponseEntity<String> updateLastLogin(@PathVariable int userId) {
        String message = eQuizPlatformService.updateLastLogin(userId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
