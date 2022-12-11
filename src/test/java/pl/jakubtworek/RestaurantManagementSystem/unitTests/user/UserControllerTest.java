package pl.jakubtworek.RestaurantManagementSystem.unitTests.user;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;
import pl.jakubtworek.RestaurantManagementSystem.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        UserDTO userDTO = new UserDTO(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of());

        // when
        when(userService.save(userRequest)).thenReturn(userDTO);

        UserResponse userCreated = userController.registerUser(userRequest).getBody();

        // then
        assertEquals("user", userCreated.getUsername());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedUser() {
        // given
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        Optional<UserDTO> expectedUser = Optional.of(new UserDTO(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of()));

        // when
        when(userService.findByUsername("user")).thenReturn(expectedUser);

        String response = userController.deleteUser("user").getBody();

        // then
        assertEquals("User with username: user was deleted", response);
    }
}
