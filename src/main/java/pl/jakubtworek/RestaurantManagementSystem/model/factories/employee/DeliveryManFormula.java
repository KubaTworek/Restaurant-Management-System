package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

@RequiredArgsConstructor
public class DeliveryManFormula implements EmployeeFormula{
    private final EmployeeRequest employeeDTO;
    private final Job job;

    @Override
    public EmployeeDTO createEmployee() {
        return EmployeeDTO.builder()
                .id(0L)
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .job(job)
                .build();
    }
}
