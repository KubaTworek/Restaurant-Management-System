package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import java.util.*;

public interface EmployeeService {
    EmployeeDTO save(EmployeeRequest theEmployee) throws JobNotFoundException;
    void deleteById(Long theId) throws EmployeeNotFoundException;
    List<EmployeeDTO> findAll();
    Optional<EmployeeDTO> findById(Long theId);
    List<EmployeeDTO> findByJob(String jobName) throws JobNotFoundException;
}