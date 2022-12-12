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
        when(jobRepository.getReferenceById(any())).thenReturn(job);

        EmployeeResponse employeeCreated = employeeController.saveEmployee(employeeRequest).getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(expectedEmployee);

        String response = employeeController.deleteEmployee(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Employee with id: d9481fe6-7843-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnAllEmployees() {
        // given
        List<Employee> expectedEmployees = createEmployees();

        // when
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployees().getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        // given
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002"))).thenReturn(expectedEmployee);

        EmployeeResponse employeeReturned = employeeController.getEmployeeById(UUID.fromString("d9481fe6-7843-11ed-a1eb-0242ac120002")).getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    void shouldReturnErrorResponse_whenEmployeeNotExist() {
        // when
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(UUID.fromString("604ae7b0-7846-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no employees in restaurant with that id: 604ae7b0-7846-11ed-a1eb-0242ac120002", exception.getMessage());
    }

    @Test
    void shouldReturnEmployeesByJob() throws Exception {
        // given
        List<Employee> expectedCooks = createCooks();

        // when
        when(employeeRepository.findByJobName("Cook")).thenReturn(expectedCooks);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployeeByJobName("Cook").getBody();

        // then
        EmployeeResponseAssertions.checkAssertionsForCooks(employeesReturned);
    }
}