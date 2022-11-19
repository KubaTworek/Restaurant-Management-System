package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;


@Component
@RequiredArgsConstructor
public class EmployeeFactory {
    public EmployeeFormula createEmployeeFormula(
            EmployeeRequest employeeRequest,
            JobDTO jobDTO
    ){
        return new EmployeeFormulaImpl(employeeRequest, jobDTO);
    }
}
