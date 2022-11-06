package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long theId);
    Employee save(EmployeeDTO theEmployee);
    void deleteById(Long theId);
    List<Employee> findByJob(String jobName);
    void addCooksToKitchen();
    void addWaitersToKitchen();
    void addDeliveriesToKitchen();
    boolean checkIfEmployeeIsNull(Long id);
    boolean checkIfJobIsNull(String name);
}