package pl.jakubtworek.RestaurantManagementSystem.service.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFormula;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private EmployeeFactory employeeFactory;
    @Mock
    private EmployeeFormula employeeFormula;
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
        employeeFactory = mock(EmployeeFactory.class);
        employeeFormula = mock(EmployeeFormula.class);
        mock(WaiterQueue.class);
        mock(DeliveryQueue.class);
        mock(CooksQueue.class);

        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                jobRepository,
                employeeFactory,
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
        EmployeeRequest employee = new EmployeeRequest(0L, "James", "Smith", "Cook");
        EmployeeDTO expectedEmployeeDTO = new EmployeeDTO(0L, "James", "Smith", new Job(1L,"Cook",List.of()), List.of());
        Employee expectedEmployee = new Employee(0L, "James", "Smith", new Job(1L,"Cook",List.of()), List.of());

        when(employeeFactory.createEmployeeFormula(any(EmployeeRequest.class))).thenReturn(employeeFormula);
        when(employeeFormula.createEmployee()).thenReturn(expectedEmployeeDTO);
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

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
