package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

public interface TypeOfOrderService {
    List<TypeOfOrder> findAll();
    TypeOfOrder findById(int theId);
    void save(TypeOfOrder theTypeOfOrder);
    void deleteById(int theId);

    TypeOfOrder findByType(String typeName);
}
