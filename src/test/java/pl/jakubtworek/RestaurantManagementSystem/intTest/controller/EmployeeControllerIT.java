package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
class EmployeeControllerIT {

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private JobRepository jobRepository;

    @Test
    void shouldReturnCreatedEmployee() throws Exception {
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        Job job = createJobCook();
        Employee expectedEmployee = createCook();

        // when
        when(employeeRepository.save(any())).thenReturn(expectedEmployee);
        when(jobRepository.findByName(any())).thenReturn(Optional.of(job));

        EmployeeResponse employeeCreated = employeeController.saveEmployee(employeeRequest).getBody();

        // then
        checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(eq(1L))).thenReturn(expectedEmployee);

        String response = employeeController.deleteEmployee(1L).getBody();

        // then
        assertEquals("Employee with id: 1 was deleted", response);
    }

    @Test
    void shouldReturnAllEmployees() {
        // given
        List<Employee> expectedEmployees = createEmployees();

        // when
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployees().getBody();

        // then
        checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        // given
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(1L)).thenReturn(expectedEmployee);

        EmployeeResponse employeeReturned = employeeController.getEmployeeById(1L).getBody();

        // then
        checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnErrorResponse_whenEmployeeNotExist() {
        // when
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(4L));

        // then
        assertEquals("There are no employees in restaurant with that id: 4", exception.getMessage());
    }

    @Test
    void shouldReturnEmployeesByJob() throws Exception {
        // given
        Optional<Job> expectedJob = Optional.of(createJobCook());
        List<Employee> expectedCooks = createCooks();

        // when
        when(jobRepository.findByName("Cook")).thenReturn(expectedJob);
        when(employeeRepository.findByJob(any())).thenReturn(expectedCooks);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployeeByJobName("Cook").getBody();

        // then
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());
    }

    @Test
    void shouldThrowException_whenJobIsNotExist() {
        // when
        Exception exception = assertThrows(JobNotFoundException.class, () -> employeeController.getEmployeeByJobName("something"));

        // then
        assertEquals("There are no job in restaurant with that name: something", exception.getMessage());
    }

    private void checkAssertionsForEmployee(EmployeeResponse employee){
        assertEquals("John", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("Cook", employee.getJob().getName());
    }

    private void checkAssertionsForCooks(List<EmployeeResponse> cooks){
        assertEquals("John", cooks.get(0).getFirstName());
        assertEquals("Smith", cooks.get(0).getLastName());
        assertEquals("Cook", cooks.get(0).getJob().getName());
    }

    private void checkAssertionsForEmployees(List<EmployeeResponse> employees){
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