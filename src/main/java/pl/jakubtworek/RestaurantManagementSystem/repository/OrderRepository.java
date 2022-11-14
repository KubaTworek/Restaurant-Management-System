package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDate(String theDate);
    List<Order> findByTypeOfOrder(TypeOfOrder theTypeOfOrder);
    List<Order> findByEmployees(Employee employee);
    List<Order> findOrdersByHourAwayIsNull();
    List<Order> findOrdersByHourAwayIsNotNull();
}
