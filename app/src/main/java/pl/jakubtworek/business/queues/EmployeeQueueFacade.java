package pl.jakubtworek.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

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

    public void addEmployeeToProperQueue(SimpleEmployee employee) {
        if (Objects.equals(employee.getJob(), Job.COOK)) cooksQueue.add(employee);
        if (Objects.equals(employee.getJob(), Job.WAITER)) waiterQueue.add(employee);
        if (Objects.equals(employee.getJob(), Job.DELIVERY)) deliveryQueue.add(employee);
    }
}
