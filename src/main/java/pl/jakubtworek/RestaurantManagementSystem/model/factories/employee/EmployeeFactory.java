package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;

@Component
@AllArgsConstructor
public class EmployeeFactory {
    private final JobRepository jobRepository;
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

    public EmployeeFormula createEmployeeFormula(EmployeeRequest employeeDTO){
        String jobName = employeeDTO.getJob();
        Job job = jobRepository.findByName(jobName).get();
        switch(jobName){
            case "Cook":
                return new CookFormula(cooksQueue, employeeDTO, job);
            case "Waiter":
                return new WaiterFormula(waiterQueue, employeeDTO, job);
            case "DeliveryMan":
                return new DeliveryManFormula(deliveryQueue, employeeDTO, job);
            default:
                return null;
        }
    }
}
