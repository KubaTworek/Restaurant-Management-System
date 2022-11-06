package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.Optional;

public interface TypeOfOrderService {
    Optional<TypeOfOrder> findByType(String typeName);
    boolean checkIfTypeOfOrderIsNull(String name);
}
