package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class EmployeeQueueFacade {
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

    public void addEmployeeToProperQueue(EmployeeDTO employeeDTO) {
        if (Objects.equals(employeeDTO.getJob().getName(), "Cook")) cooksQueue.add(employeeDTO);
        if (Objects.equals(employeeDTO.getJob().getName(), "Waiter")) waiterQueue.add(employeeDTO);
        if (Objects.equals(employeeDTO.getJob().getName(), "DeliveryMan")) deliveryQueue.add(employeeDTO);
    }
}
