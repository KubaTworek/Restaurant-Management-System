package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
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
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        EmployeeDTO expectedEmployeeDTO = createCook().convertEntityToDTO();
        Job job = createJobCook();
        Employee expectedEmployee = createCook();

        when(employeeFactory.createEmployee(any(EmployeeRequest.class), any(JobDTO.class))).thenReturn(expectedEmployeeDTO);
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);
        when(jobRepository.findByName(any())).thenReturn(Optional.of(job));

        // when
        EmployeeDTO employeeCreated = employeeService.save(employeeRequest);

        // then
        checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    void verifyIsEmployeeIsDeleted() throws EmployeeNotFoundException {
        // given
        Employee expectedEmployee = createCook();

        // when
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(expectedEmployee));

        employeeService.deleteById(1L);

        // then
        verify(employeeRepository).delete(any());
    }

    @Test
    void shouldReturnAllEmployees(){
        // given
        List<Employee> employees = createEmployees();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployeeById(){
        Optional<Employee> employee = Optional.of(createCook());
        when(employeeRepository.findById(1L)).thenReturn(employee);

        // when
        EmployeeDTO employeeReturned = employeeService.findById(1L).orElse(null);

        // then
        checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnEmployeesByJobName() throws JobNotFoundException {
        // given
        List<Employee> employees = createEmployees();
        Job job = createJobCook();

        // when
        when(jobRepository.findByName("Job")).thenReturn(Optional.of(job));
        when(employeeRepository.findByJob(job)).thenReturn(employees);

        List<EmployeeDTO> employeesReturned = employeeService.findByJob("Job");

        // then
        checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldThrowException_whenJobNotExist() {
        // when
        Exception exception = assertThrows(JobNotFoundException.class, () -> employeeService.findByJob("Random"));

        // then
        assertEquals("There are no job in restaurant with that name: Random", exception.getMessage());
    }

    private void checkAssertionsForEmployee(EmployeeDTO employee){
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Cook", employee.getJob().getName());
    }

    private void checkAssertionsForCooks(List<EmployeeDTO> cooks){
        assertEquals("John", cooks.get(0).getFirstName());
        assertEquals("Smith", cooks.get(0).getLastName());
        assertEquals("Cook", cooks.get(0).getJob().getName());
    }

    private void checkAssertionsForEmployees(List<EmployeeDTO> employees){
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals("Cook", employees.get(0).getJob().getName());

        assertEquals("James", employees.get(1).getFirstName());
        assertEquals("Patel", employees.get(1).getLastName());
        assertEquals("Waiter", employees.get(1).getJob().getName());

        assertEquals("Ann", employees.get(2).getFirstName());
        assertEquals("Mary", employees.get(2).getLastName());
        assertEquals("DeliveryMan", employees.get(2).getJob().getName());
    }
}
