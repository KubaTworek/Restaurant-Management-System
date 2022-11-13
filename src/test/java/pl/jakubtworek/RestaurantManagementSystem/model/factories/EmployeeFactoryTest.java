package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeFactoryTest {
    @Mock
    private JobRepository jobRepository;
    @Mock
    private CooksQueue cooksQueue;
    @Mock
    private WaiterQueue waiterQueue;
    @Mock
    private DeliveryQueue deliveryQueue;

    private EmployeeFactory employeeFactory;

    @BeforeEach
    public void setUp() {
        jobRepository = mock(JobRepository.class);
        cooksQueue = mock(CooksQueue.class);
        waiterQueue = mock(WaiterQueue.class);
        deliveryQueue = mock(DeliveryQueue.class);

        employeeFactory = new EmployeeFactory(
                jobRepository,
                cooksQueue,
                waiterQueue,
                deliveryQueue);

        Optional<Job> cook = Optional.of(new Job(1L, "Cook", null));
        Optional<Job> waiter = Optional.of(new Job(2L, "Waiter", null));
        Optional<Job> deliveryman = Optional.of(new Job(3L, "DeliveryMan", null));

        when(jobRepository.findByName("Cook")).thenReturn(cook);
        when(jobRepository.findByName("Waiter")).thenReturn(waiter);
        when(jobRepository.findByName("DeliveryMan")).thenReturn(deliveryman);
    }

    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        EmployeeRequest employeeDTO = new EmployeeRequest(1L, "John", "Smith", "Cook");

        // when
        Employee employee = employeeFactory.createEmployee(employeeDTO);

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Cook", employee.getJob().getName());
    }

    @Test
    public void shouldReturnWaiter_whenProvideWaiter(){
        // given
        EmployeeRequest employeeDTO = new EmployeeRequest(1L, "John", "Smith", "Waiter");

        // when
        Employee employee = employeeFactory.createEmployee(employeeDTO);

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Waiter", employee.getJob().getName());
    }

    @Test
    public void shouldReturnDeliveryman_whenProvideDeliveryman(){
        // given
        EmployeeRequest employeeDTO = new EmployeeRequest(1L, "John", "Smith", "DeliveryMan");

        // when
        Employee employee = employeeFactory.createEmployee(employeeDTO);

        // then
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("DeliveryMan", employee.getJob().getName());
    }

    @Test
    public void shouldReturnNull_whenProvideWrongJob(){
        // given
        EmployeeRequest employeeDTO = new EmployeeRequest(1L, "John", "Smith", "Wrong");

        // when
        Employee employee = employeeFactory.createEmployee(employeeDTO);

        // then
        assertNull(employee);
    }
}
