package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;

@RequiredArgsConstructor
public class EmployeeFormulaImpl implements EmployeeFormula{
    private final EmployeeRequest employeeDTO;
    private final JobDTO job;

    @Override
    public EmployeeDTO createEmployee(
    ) {
        String firstName = employeeDTO.getFirstName();
        String lastName = employeeDTO.getLastName();

        return EmployeeDTO.builder()
                .id(0L)
                .firstName(firstName)
                .lastName(lastName)
                .job(job)
                .build();
    }
}
