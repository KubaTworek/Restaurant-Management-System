package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByDate(String date);
    Optional<List<Order>> findByTypeOfOrder(TypeOfOrder typeOfOrder);
    //Optional<Order> findByEmployees(List<Employee> employees);
    Optional<List<Order>> findOrdersByHourAwayIsNull();
    Optional<List<Order>> findOrdersByHourAwayIsNotNull();
}
