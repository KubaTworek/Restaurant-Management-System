package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

public interface EmployeeFormula {
    EmployeeDTO createEmployee();
}
