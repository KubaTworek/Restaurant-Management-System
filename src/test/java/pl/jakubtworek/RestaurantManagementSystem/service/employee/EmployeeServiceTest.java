package pl.jakubtworek.RestaurantManagementSystem.service.employee;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {


    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private CooksQueue cooksQueue;
    @Mock
    private WaiterQueue waiterQueue;
    @Mock
    private DeliveryQueue deliveryQueue;

    private EmployeeServiceImpl employeeService;



    @BeforeEach
    public void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        jobRepository = mock(JobRepository.class);
        cooksQueue = mock(CooksQueue.class);
        waiterQueue = mock(WaiterQueue.class);
        deliveryQueue = mock(DeliveryQueue.class);

        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                jobRepository,
                cooksQueue,
                waiterQueue,
                deliveryQueue);
    }

    @Test
    public void shouldReturnAllEmployees(){
        // given
        List<Employee> employees = createEmployees();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<Employee> employeesReturned = employeeService.findAll();

        // then
        assertEquals(3,employeesReturned.size());
    }

    @Test
    public void shouldReturnOneEmployee(){
        Optional<Employee> employee = Optional.of(new Employee());
        when(employeeRepository.findById(1L)).thenReturn(employee);

        // when
        Optional<Employee> employeeReturned = employeeService.findById(1L);

        // then
        assertNotNull(employeeReturned);
    }

    @Test
    public void shouldReturnCreatedEmployee(){
        // given
        Employee employee = spy(new Employee());
        Job job = spy(new Job());
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employee.getJob()).thenReturn(job);
        when(job.getId()).thenReturn(eq(1L));

        // when
        Employee employeeReturned = employeeService.save(employee);

        // then
        assertNotNull(employeeReturned);
    }


    @Test
    public void verifyIsEmployeeIsDeleted(){
        // when
        employeeService.deleteById(1L);
        // then

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    public void shouldReturnProperJob(){
        // given
        List<Employee> employees = createEmployees();
        Optional<Job> job = Optional.of(new Job(1L,"Job",new ArrayList<>()));
        when(jobRepository.findByName("Job")).thenReturn(job);
        when(employeeRepository.findByJob(job.get())).thenReturn(employees);

        // when
        List<Employee> employeesReturned = employeeService.findByJob("Job");

        // then
        assertEquals(3,employeesReturned.size());
    }

    private List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<>();
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Employee employee3 = new Employee();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        return employees;
    }
}
