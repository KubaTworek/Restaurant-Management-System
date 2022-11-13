package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long theId);
    Employee save(EmployeeRequest theEmployee);
    void deleteById(Long theId);
    List<Employee> findByJob(String jobName);
    boolean checkIfEmployeeIsNull(Long id);
    boolean checkIfJobIsNull(String name);
}