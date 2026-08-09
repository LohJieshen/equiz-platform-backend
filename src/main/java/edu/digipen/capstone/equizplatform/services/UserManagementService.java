package edu.digipen.capstone.equizplatform.services;

import edu.digipen.capstone.equizplatform.entities.User;
import edu.digipen.capstone.equizplatform.models.UserBasicProfileInfo;
import edu.digipen.capstone.equizplatform.models.UserCredentials;
import edu.digipen.capstone.equizplatform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    /**
     * Authenticates the user's login credentials by first checking if the userId exists, then checks if password
     * matches between client and database.
     *
     * @param userCredentialsClient - user credential information, containing userId, password (isLecturer is null at
     *                             this point) sent from client side.
     * @return true if the authentication is successful. False otherwise.
     */
    public boolean authenticateUserLogin(UserCredentials userCredentialsClient) {

        int clientUserId = userCredentialsClient.getUserId();

        // Retrieves UserCredential item based on user id
        Optional<User> opUserDB = userRepository.findById(clientUserId);

        if (opUserDB.isEmpty()) {
            log.info("userId '{}' | User does not exist", clientUserId);
            return false;
        }

        User userDB = opUserDB.get();

        return passwordEncoder.matches(userCredentialsClient.getPassword(), userDB.getPasswordHash());
    }


    /**
     * Search for the basic user information, which consists of user's first name and last login date.
     *
     * @param userId - the unique user identification number for a given user.
     * @return UserBasicProfileInfo DTO.
     */
    public UserBasicProfileInfo getUserBasicInfo(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found."));

        return new UserBasicProfileInfo(user.getFirstName(),
                user.getCourse().getCourseId(),
                user.isLecturer(),
                user.getLastLoginDate());
    }

    public boolean checkLecturerAccess(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found."));

        return user.isLecturer();
    }

    public String updateLastLogin(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found"));
        user.setLastLoginDate(LocalDate.now());
        userRepository.save(user);

        return "Last Login Date updated";
    }

}
