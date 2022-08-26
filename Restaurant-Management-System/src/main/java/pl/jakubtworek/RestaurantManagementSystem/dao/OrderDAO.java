package pl.jakubtworek.RestaurantManagementSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByDate(String theDate);
    //List<Order> findByEmployee(Employee theEmployee);
    List<Order> findByTypeOfOrder(TypeOfOrder theTypeOfOrder);
}
