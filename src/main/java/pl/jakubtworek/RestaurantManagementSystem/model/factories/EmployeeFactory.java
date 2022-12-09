package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;


@Component
public class EmployeeFactory {
    public EmployeeDTO createEmployee(
            EmployeeRequest employeeRequest,
            JobDTO jobDTO
    ) {
        String firstName = employeeRequest.getFirstName();
        String lastName = employeeRequest.getLastName();

        return EmployeeDTO.builder()
                .id(0L)
                .firstName(firstName)
                .lastName(lastName)
                .job(jobDTO)
                .build();
    }
}
