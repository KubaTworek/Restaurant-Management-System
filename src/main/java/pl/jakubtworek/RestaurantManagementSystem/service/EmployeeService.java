package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int theId);
    void save(Employee theEmployee);
    void deleteById(int theId);

    List<Employee> findByJob(String jobName);
    Job findJobByName(String jobName);
    void addCooksToKitchen();
    void addWaitersToKitchen();
    void addDeliveriesToKitchen();
}
