package pl.jakubtworek.RestaurantManagementSystem.model.factories.employee;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

@RequiredArgsConstructor
public class DeliveryManFormula implements EmployeeFormula{
    private final DeliveryQueue deliveryQueue;
    private final EmployeeRequest employeeDTO;
    private final Job job;

    @Override
    public Employee createEmployee() {
        Employee employee = Employee.builder()
                .id(0L)
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .job(job)
                .build();
        deliveryQueue.add(employee);
        return employee;
    }
}
