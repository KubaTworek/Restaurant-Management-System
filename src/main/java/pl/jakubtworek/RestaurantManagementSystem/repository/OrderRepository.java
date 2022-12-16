package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findOrdersByHourAwayIsNull();

    List<Order> findOrdersByHourAwayIsNotNull();

    List<Order> findByUserUsername(String username);

    List<Order> findOrdersByHourAwayIsNullAndUserUsername(String username);

    List<Order> findOrdersByHourAwayIsNotNullAndUserUsername(String username);
}