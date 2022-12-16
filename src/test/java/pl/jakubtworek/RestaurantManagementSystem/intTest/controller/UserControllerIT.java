package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.*;

@SpringBootTest
class UserControllerIT {
    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthoritiesRepository authoritiesRepository;

    @Test
    void shouldReturnCreatedUser() {
        // given
        UserRequest userRequest = new UserRequest("user", "user", "user");
        AuthoritiesDTO authority = createUserRole().convertEntityToDTO();
        UserDTO userDTO = createUser().convertEntityToDTO();

        // when
        when(userRepository.save(any())).thenReturn(userDTO.convertDTOToEntity());
        when(authoritiesRepository.findAuthoritiesByAuthority(any())).thenReturn(Optional.ofNullable(authority.convertDTOToEntity()));

        UserResponse userCreated = userController.registerUser(userRequest).getBody();

        // then
        assertEquals("user", userCreated.getUsername());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedUser() {
        // given
        Optional<User> user = Optional.of(createUser());

        // when
        when(userRepository.findByUsername("user")).thenReturn(user);

        String response = userController.deleteUser("user").getBody();

        // then
        assertEquals("User with username: user was deleted", response);
    }
}
