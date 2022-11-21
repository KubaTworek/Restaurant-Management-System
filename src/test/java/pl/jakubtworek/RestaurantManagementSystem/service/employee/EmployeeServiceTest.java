package pl.jakubtworek.RestaurantManagementSystem.service.employee;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFormula;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private EmployeeFactory employeeFactory;
    @Mock
    private EmployeeFormula employeeFormula;


    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    public void shouldReturnAllEmployees(){
        // given
        List<Employee> employees = createEmployees();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        assertEquals(3,employeesReturned.size());
    }

    @Test
    public void shouldReturnOneEmployee(){
        Optional<Employee> employee = Optional.of(new Employee());
        when(employeeRepository.findById(1L)).thenReturn(employee);

        // when
        Optional<EmployeeDTO> employeeReturned = employeeService.findById(1L);

        // then
        assertNotNull(employeeReturned);
    }

    @Test
    public void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employeeRequest = createCookRequest();
        JobDTO job = createCookDTO();
        EmployeeDTO expectedEmployeeDTO = createEmployeeDTO().get();
        Employee expectedEmployee = createEmployee().get();

        when(employeeFactory.createEmployeeFormula(any(EmployeeRequest.class), any(JobDTO.class))).thenReturn(employeeFormula);
        when(employeeFormula.createEmployee()).thenReturn(expectedEmployeeDTO);
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

        // when
        EmployeeDTO employeeReturned = employeeService.save(employeeRequest, job);

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
        Optional<Employee> employees = createCooks();
        Job job = createCook().get();


        // when
        when(jobRepository.findByName("Job")).thenReturn(Optional.of(job));
        when(employeeRepository.findByJob(job)).thenReturn(employees);

        List<EmployeeDTO> employeesReturned = employeeService.findByJob(job);

        // then
        assertEquals(3,employeesReturned.size());
    }
}
