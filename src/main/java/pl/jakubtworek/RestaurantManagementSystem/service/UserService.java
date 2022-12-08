package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    Optional<UserDTO> findByUsername(String username);
}
