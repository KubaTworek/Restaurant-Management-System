package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;


@Component
@RequiredArgsConstructor
public class EmployeeFactory {
    public EmployeeFormula createEmployeeFormula(
            EmployeeRequest employeeDTO,
            Job job
    ){
        return new EmployeeFormulaImpl(employeeDTO, job);
    }
}
