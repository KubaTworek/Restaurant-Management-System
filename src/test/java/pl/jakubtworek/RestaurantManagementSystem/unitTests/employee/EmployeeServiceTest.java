package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
    void setup(){
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
    void shouldReturnAllEmployees(){
        // given
        List<Employee> employees = createEmployees();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        assertEquals(3,employeesReturned.size());
    }

    @Test
    void shouldReturnOneEmployee(){
        Optional<Employee> employee = Optional.of(createCook());
        when(employeeRepository.findById(1L)).thenReturn(employee);

        // when
        Optional<EmployeeDTO> employeeReturned = employeeService.findById(1L);

        // then
        assertNotNull(employeeReturned);
    }

    @Test
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        EmployeeDTO expectedEmployeeDTO = createCook().convertEntityToDTO();
        Employee expectedEmployee = createCook();

        when(employeeFactory.createEmployee(any(EmployeeRequest.class), any(JobDTO.class))).thenReturn(expectedEmployeeDTO);
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

        // when
        EmployeeDTO employeeReturned = employeeService.save(employeeRequest);

        // then
        assertNotNull(employeeReturned);
    }


    @Test
    void verifyIsEmployeeIsDeleted() throws EmployeeNotFoundException {
        // given
        Employee expectedEmployee = createCook();

        // when
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(expectedEmployee));

        employeeService.deleteById(1L);

        // then
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void shouldReturnProperJob() throws JobNotFoundException {
        // given
        List<Employee> employees = createEmployees();
        Job job = createJobCook();

        // when
        when(jobRepository.findByName("Job")).thenReturn(Optional.of(job));
        when(employeeRepository.findByJob(job)).thenReturn(employees);

        List<EmployeeDTO> employeesReturned = employeeService.findByJob("Job");

        // then
        assertEquals(3, employeesReturned.size());
    }
}
