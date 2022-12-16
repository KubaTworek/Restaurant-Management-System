package pl.jakubtworek.RestaurantManagementSystem.unitTests.user;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.createUser;

class UserControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);

        userController = new UserController(
                userService
        );
    }

    @Test
    void shouldReturnCreatedUser() {
        // given
        UserRequest userRequest = new UserRequest("user", "user", "user");
        UserDTO expectedUser = createUser().convertEntityToDTO();

        // when
        when(userService.save(userRequest)).thenReturn(expectedUser);

        UserResponse userCreated = userController.registerUser(userRequest).getBody();

        // then
        assertEquals("user", userCreated.getUsername());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedUser() {
        // given
        Optional<UserDTO> expectedUser = Optional.of(createUser().convertEntityToDTO());

        // when
        when(userService.findByUsername("user")).thenReturn(expectedUser);

        String response = userController.deleteUser("user").getBody();

        // then
        assertEquals("User with username: user was deleted", response);
    }
}