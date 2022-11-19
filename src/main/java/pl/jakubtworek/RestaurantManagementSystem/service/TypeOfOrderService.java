package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.Optional;

public interface TypeOfOrderService {
    Optional<TypeOfOrderDTO> findByType(String typeName);
}
