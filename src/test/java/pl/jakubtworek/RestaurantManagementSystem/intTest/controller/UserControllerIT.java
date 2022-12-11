package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerIT {

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturnCreatedUser(){
        // given
        UserRequest userRequest = new UserRequest("user", "user", "user");
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        UserDTO userDTO = new UserDTO(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of());

        // when
        when(userRepository.save(any())).thenReturn(userDTO.convertDTOToEntity());

        UserResponse userCreated = userController.registerUser(userRequest).getBody();

        // then
        assertEquals("user", userCreated.getUsername());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedUser() {
        // given
        Authorities authority = new Authorities(UUID.fromString("a1437b9c-798b-11ed-a1eb-0242ac120002"), "user", List.of());
        Optional<User> user = Optional.of(new User(UUID.fromString("9c9b3ed6-798b-11ed-a1eb-0242ac120002"), "user", "$2a$12$iWBt07UWbcIxuPpVUS2ssO78P9W0ezJUK5CW6c7b4X6PvT33vctv2", authority, List.of()));

        // when
        when(userRepository.findByUsername("user")).thenReturn(user);

        String response = userController.deleteUser("user").getBody();

        // then
        assertEquals("User with username: user was deleted", response);
    }
}
