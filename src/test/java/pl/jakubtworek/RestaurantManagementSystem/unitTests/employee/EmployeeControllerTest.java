package pl.jakubtworek.RestaurantManagementSystem.unitTests.employee;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
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
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());

        assertEquals("James", employeesReturned.get(1).getFirstName());
        assertEquals("Patel", employeesReturned.get(1).getLastName());
        assertEquals("Waiter", employeesReturned.get(1).getJob().getName());

        assertEquals("Ann", employeesReturned.get(2).getFirstName());
        assertEquals("Mary", employeesReturned.get(2).getLastName());
        assertEquals("DeliveryMan", employeesReturned.get(2).getJob().getName());
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        // given
        Optional<EmployeeDTO> expectedEmployee = Optional.of(createCook().convertEntityToDTO());

        // when
        when(employeeService.findById(eq(1L))).thenReturn(expectedEmployee);

        EmployeeResponse employeeReturned = employeeController.getEmployeeById(1L).getBody();

        // then
        assertEquals("John", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingEmployee() {
        // when
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(4L));

        // then
        assertEquals("There are no employees in restaurant with that id: 4", exception.getMessage());
    }

    @Test
    void shouldReturnCreatedEmployee() throws Exception {
        // given
        EmployeeDTO returnedEmployee = createCook().convertEntityToDTO();
        EmployeeRequest employee = createCookRequest();

        // when
        when(employeeService.save(employee)).thenReturn(returnedEmployee);

        EmployeeResponse employeeReturned = employeeController.saveEmployee(employee).getBody();

        // then
        assertEquals("John", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<EmployeeDTO> expectedEmployee = Optional.of(createCook().convertEntityToDTO());

        // when
        when(employeeService.findById(eq(1L))).thenReturn(expectedEmployee);

        String response = employeeController.deleteEmployee(1L).getBody();

        // then
        assertEquals("Employee with id: 1 was deleted", response);
    }

    @Test
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        // given
        List<EmployeeDTO> expectedCooks = createCooks()
                .stream()
                .map(Employee::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(employeeService.findByJob(any())).thenReturn(expectedCooks);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployeeByJobName("Cook").getBody();

        // then
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());
    }

    @Test
    void shouldReturnResponse_whenWrongJobNameIsPassed() {
        // when
        Exception exception = assertThrows(JobNotFoundException.class, () -> employeeController.getEmployeeByJobName("Random"));

        // then
        assertEquals("There are no job in restaurant with that name: Random", exception.getMessage());
    }
}
