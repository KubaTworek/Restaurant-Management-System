package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

public interface TypeOfOrderService {
    TypeOfOrder findByType(String typeName);
}
