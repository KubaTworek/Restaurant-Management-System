package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.EmployeeNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    private EmployeeController employeeController;

    @BeforeEach
    void setup(){
        employeeService = mock(EmployeeService.class);

        employeeController = new EmployeeController(
                employeeService
        );
    }

    @Test
    void shouldReturnCreatedEmployee() throws Exception {
        // given
        EmployeeDTO returnedEmployee = createCook().convertEntityToDTO();
        EmployeeRequest employee = createCookRequest();

        // when
        when(employeeService.save(employee)).thenReturn(returnedEmployee);

        EmployeeResponse employeeCreated = employeeController.saveEmployee(employee).getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<EmployeeDTO> expectedEmployee = Optional.of(createCook().convertEntityToDTO());

        // when
        when(employeeService.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(expectedEmployee);

        String response = employeeController.deleteEmployee(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Employee with id: d9481fe6-7843-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnAllEmployees() {
        // given
        List<EmployeeDTO> expectedEmployees = createEmployees()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(employeeService.findAll()).thenReturn(expectedEmployees);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployees().getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        // given
        Optional<EmployeeDTO> expectedEmployee = Optional.of(createCook().convertEntityToDTO());

        // when
        when(employeeService.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(expectedEmployee);

        EmployeeResponse employeeReturned = employeeController.getEmployeeById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldThrowException_whenEmployeeNotExist() {
        // when
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(UUID.fromString("277584b0-7844-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no employees in restaurant with that id: 277584b0-7844-11ed-a1eb-0242ac120002", exception.getMessage());
    }

    @Test
    void shouldReturnEmployeesByJobName() throws Exception {
        // given
        List<EmployeeDTO> expectedCooks = createCooks()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(employeeService.findByJob(any())).thenReturn(expectedCooks);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployeeByJobName("Cook").getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForCooks(employeesReturned);
    }
}
