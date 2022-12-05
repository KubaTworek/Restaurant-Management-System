package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByDate(String date);
    Optional<Order> findByTypeOfOrder(TypeOfOrder typeOfOrder);
    //Optional<Order> findByEmployees(List<Employee> employees);
    Optional<Order> findOrdersByHourAwayIsNull();
    Optional<Order> findOrdersByHourAwayIsNotNull();
}
