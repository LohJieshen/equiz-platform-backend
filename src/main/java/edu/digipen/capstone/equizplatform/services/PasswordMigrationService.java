package edu.digipen.capstone.equizplatform.services;

import edu.digipen.capstone.equizplatform.entities.User;
import edu.digipen.capstone.equizplatform.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordMigrationService implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<User> userList = userRepository.findAll();

        userList.forEach(this::hashPassword);

        userRepository.saveAll(userList);
        log.info("Hashing operation completed");
    }

    private void hashPassword(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        user.setPassword(null);
    }
}
