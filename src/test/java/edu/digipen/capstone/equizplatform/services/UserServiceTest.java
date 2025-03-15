package edu.digipen.capstone.equizplatform.services;

import edu.digipen.capstone.equizplatform.entities.User;
import edu.digipen.capstone.equizplatform.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository mockUserRepository;

    @Test
    void testShouldReturnSuccessMessageOnUpdatingLastLoginDate() {

        // Given
        Integer userId = 100001;
        User userWithOldLoginDate = getMockUserWithOldLoginDate(userId);
        User updatedUser = getMockUserWithUpdatedLoginDate(userId);
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(userWithOldLoginDate));
        when(mockUserRepository.save(userWithOldLoginDate)).thenReturn(updatedUser);

        // When
        String message = userService.updateLastLogin(userId);

        // Then
        assertEquals(LocalDate.now(), updatedUser.getLastLoginDate());
        assertEquals("Last Login Date updated", message);
    }

    private User getMockUserWithOldLoginDate(Integer userId) {

        User user = new User();
        user.setUserId(userId);
        user.setLastLoginDate(LocalDate.of(2000, 8, 31));

        return user;
    }

    private User getMockUserWithUpdatedLoginDate(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        user.setLastLoginDate(LocalDate.now());

        return user;
    }
}
