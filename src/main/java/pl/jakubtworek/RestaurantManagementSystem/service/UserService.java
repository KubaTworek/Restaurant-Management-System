package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO save(UserRequest userRequest);
    Optional<UserDTO> findByUsername(String username);
}
