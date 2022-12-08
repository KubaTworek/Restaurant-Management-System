package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import java.util.*;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    Optional<EmployeeDTO> findById(Long theId);
    EmployeeDTO save(EmployeeRequest theEmployee) throws JobNotFoundException;
    void deleteById(Long theId);
    List<EmployeeDTO> findByJob(String jobName) throws JobNotFoundException;
}