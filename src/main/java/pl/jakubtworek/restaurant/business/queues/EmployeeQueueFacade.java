package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.employee.Job;

import java.util.Objects;

@Component
public class EmployeeQueueFacade {
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

    EmployeeQueueFacade(final CooksQueue cooksQueue, final WaiterQueue waiterQueue, final DeliveryQueue deliveryQueue) {
        this.cooksQueue = cooksQueue;
        this.waiterQueue = waiterQueue;
        this.deliveryQueue = deliveryQueue;
    }

    public void addEmployeeToProperQueue(EmployeeDto employeeDTO) {
        if (Objects.equals(employeeDTO.getJob(), Job.COOK)) cooksQueue.add(employeeDTO);
        if (Objects.equals(employeeDTO.getJob(), Job.WAITER)) waiterQueue.add(employeeDTO);
        if (Objects.equals(employeeDTO.getJob(), Job.DELIVERY)) deliveryQueue.add(employeeDTO);
    }
}
