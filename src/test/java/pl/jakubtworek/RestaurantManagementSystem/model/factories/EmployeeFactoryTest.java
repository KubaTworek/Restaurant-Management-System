package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

public class EmployeeFactoryTest {
    private EmployeeFactory employeeFactory;

    @BeforeEach
    public void setUp() {
        employeeFactory = new EmployeeFactory();
    }

    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        JobDTO job = createCookDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployeeFormula(employeeRequest, job).createEmployee();

        // then
        assertEquals("James", employee.getFirstName());
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
        assertEquals("James", employee.getFirstName());
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
        assertEquals("James", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("DeliveryMan", employee.getJob().getName());
    }
}
