package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

class EmployeeFactoryTest {
    private EmployeeFactory employeeFactory;

    @BeforeEach
    void setUp() {
        employeeFactory = new EmployeeFactory();
    }

    @Test
    void shouldReturnCook_whenProvideCook(){
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        JobDTO job = createJobCook().convertEntityToDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployee(employeeRequest, job);

        // then
        assertEquals("James", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Cook", employee.getJob().getName());
    }

    @Test
    void shouldReturnWaiter_whenProvideWaiter(){
        // given
        EmployeeRequest employeeRequest = createWaiterRequest();
        JobDTO job = createJobWaiter().convertEntityToDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployee(employeeRequest, job);

        // then
        assertEquals("James", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Waiter", employee.getJob().getName());
    }

    @Test
    void shouldReturnDeliveryman_whenProvideDeliveryman(){
        // given
        EmployeeRequest employeeRequest = createDeliveryRequest();
        JobDTO job = createJobDeliveryman().convertEntityToDTO();

        // when
        EmployeeDTO employee = employeeFactory.createEmployee(employeeRequest, job);

        // then
        assertEquals("James", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("DeliveryMan", employee.getJob().getName());
    }
}
