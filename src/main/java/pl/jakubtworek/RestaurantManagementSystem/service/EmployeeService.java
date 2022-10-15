package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(int theId);
    void save(Employee theEmployee);
    void deleteById(int theId);
    List<Employee> findByJob(String jobName);
    Optional<Job> findJobByName(String jobName);
    void addCooksToKitchen();
    void addWaitersToKitchen();
    void addDeliveriesToKitchen();
}