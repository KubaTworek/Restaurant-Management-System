package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

public interface TypeOfOrderService {
    TypeOfOrder findByType(String typeName);
}
