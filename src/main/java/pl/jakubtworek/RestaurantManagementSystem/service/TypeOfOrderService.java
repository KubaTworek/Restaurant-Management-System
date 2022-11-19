package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;

import java.util.Optional;

public interface TypeOfOrderService {
    Optional<TypeOfOrderDTO> findByType(String typeName);
}
