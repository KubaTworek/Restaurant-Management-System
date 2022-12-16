package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private EmployeeFactory employeeFactory;
    @Mock
    private EmployeeQueueFacade employeeQueueFacade;

    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        jobRepository = mock(JobRepository.class);
        employeeFactory = mock(EmployeeFactory.class);
        employeeQueueFacade = mock(EmployeeQueueFacade.class);

        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                jobRepository,
                employeeFactory,
                employeeQueueFacade
        );
    }

    @Test
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        EmployeeDTO expectedEmployeeDTO = createCook().convertEntityToDTO();
        Job job = createJobCook();
        Employee expectedEmployee = createCook();

        when(employeeFactory.createEmployee(any(), any())).thenReturn(expectedEmployeeDTO);
        when(employeeRepository.save(any())).thenReturn(expectedEmployee);
        when(jobRepository.findByName(any())).thenReturn(Optional.of(job));
        when(jobRepository.getReferenceById(any())).thenReturn(job);

        // when
        EmployeeDTO employeeCreated = employeeService.save(employeeRequest);

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    void verifyIsEmployeeIsDeleted() throws EmployeeNotFoundException {
        // given
        Employee expectedEmployee = createCook();

        // when
        when(employeeRepository.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(Optional.of(expectedEmployee));

        employeeService.deleteById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"));

        // then
        verify(employeeRepository).delete(any());
    }

    @Test
    void shouldReturnAllEmployees() {
        // given
        List<Employee> employees = createEmployees();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployeeById() {
        Optional<Employee> employee = Optional.of(createCook());
        when(employeeRepository.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(employee);

        // when
        EmployeeDTO employeeReturned = employeeService.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnEmployeesByJobName() {
        // given
        List<Employee> employees = createEmployees();

        // when
        when(employeeRepository.findByJobName("Job")).thenReturn(employees);

        List<EmployeeDTO> employeesReturned = employeeService.findByJob("Job");

        // then
        EmployeeDTOAssertions.checkAssertionsForCooks(employeesReturned);
    }
}