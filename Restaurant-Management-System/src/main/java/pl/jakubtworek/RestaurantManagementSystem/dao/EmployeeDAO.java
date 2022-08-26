package pl.jakubtworek.RestaurantManagementSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;

import java.util.List;

public interface EmployeeDAO extends JpaRepository<Employee, Integer> {
    List<Employee> findByJob(Job theJob);
}
