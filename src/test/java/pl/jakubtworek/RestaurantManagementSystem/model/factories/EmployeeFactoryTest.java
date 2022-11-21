package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

public class EmployeeFactoryTest {
    @Autowired
    private EmployeeFactory employeeFactory;


    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        JobDTO job = createCookDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployeeFormula(employeeRequest, job).createEmployee();

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Cook", employee.getJob().getName());
    }

    @Test
    public void shouldReturnWaiter_whenProvideWaiter(){
        // given
        EmployeeRequest employeeRequest = createWaiterRequest();
        JobDTO job = createWaiterDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployeeFormula(employeeRequest, job).createEmployee();

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Waiter", employee.getJob().getName());
    }

    @Test
    public void shouldReturnDeliveryman_whenProvideDeliveryman(){
        // given
        EmployeeRequest employeeRequest = createDeliveryManRequest();
        JobDTO job = createDeliveryManDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployeeFormula(employeeRequest, job).createEmployee();

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("DeliveryMan", employee.getJob().getName());
    }
}
