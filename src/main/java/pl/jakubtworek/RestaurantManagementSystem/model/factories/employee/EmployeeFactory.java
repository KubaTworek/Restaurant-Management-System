package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;

@Component
@RequiredArgsConstructor
public class EmployeeFactory {
    private final JobRepository jobRepository;

    public EmployeeFormula createEmployeeFormula(EmployeeRequest employeeDTO){
        String jobName = employeeDTO.getJob();
        Job job = jobRepository.findByName(jobName).get();
        switch(jobName){
            case "Cook":
                return new CookFormula(employeeDTO, job);
            case "Waiter":
                return new WaiterFormula(employeeDTO, job);
            case "DeliveryMan":
                return new DeliveryManFormula(employeeDTO, job);
            default:
                return null;
        }
    }
}
