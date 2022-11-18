package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByDate(String date);
    Optional<Order> findByTypeOfOrder(TypeOfOrder typeOfOrder);
    Optional<Order> findByEmploye(Employee employee);
    Optional<Order> findOrdersByHourAwayIsNull();
    Optional<Order> findOrdersByHourAwayIsNotNull();
}
