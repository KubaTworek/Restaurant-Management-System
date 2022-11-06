package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.GetEmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;

@Component
public class EmployeeFactory {
    private final JobRepository jobRepository;
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

    public EmployeeFactory(JobRepository jobRepository, CooksQueue cooksQueue, WaiterQueue waiterQueue, DeliveryQueue deliveryQueue) {
        this.jobRepository = jobRepository;
        this.cooksQueue = cooksQueue;
        this.waiterQueue = waiterQueue;
        this.deliveryQueue = deliveryQueue;
    }

    public Employee createEmployee(GetEmployeeDTO employeeDTO) {
        String jobName = employeeDTO.getJob();
        switch(jobName){
            case "Cook":
                return createCook(employeeDTO);
            case "Waiter":
                return createWaiter(employeeDTO);
            case "DeliveryMan":
                return createDeliveryMan(employeeDTO);
            default:
                return null;
        }
    }

    private Employee createCook(GetEmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(0L);
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setJob(jobRepository.findByName("Cook").get());
        cooksQueue.add(employee);
        return employee;
    }

    private Employee createWaiter(GetEmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(0L);
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setJob(jobRepository.findByName("Waiter").get());
        waiterQueue.add(employee);
        return employee;
    }

    private Employee createDeliveryMan(GetEmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(0L);
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setJob(jobRepository.findByName("DeliveryMan").get());
        deliveryQueue.add(employee);
        return employee;
    }
}
