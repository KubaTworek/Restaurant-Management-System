package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByDate(String date);
    List<Order> findByTypeOfOrderType(String typeOfOrder);
    List<Order> findByEmployeesId(UUID employeeId);
    List<Order> findByDateAndEmployeesId(String date, UUID employeeId);
    List<Order> findByDateAndTypeOfOrderType(String date, String typeOfOrder);
    List<Order> findByTypeOfOrderTypeAndEmployeesId(String typeOfOrder, UUID employeeId);
    List<Order> findByDateAndEmployeesIdAndTypeOfOrderType(String date, UUID employeeId, String typeOfOrder);
    List<Order> findOrdersByHourAwayIsNull();
    List<Order> findOrdersByHourAwayIsNotNull();
}
