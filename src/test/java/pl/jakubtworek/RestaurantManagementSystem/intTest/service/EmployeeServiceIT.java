package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@Transactional
class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Sql(statements = "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')")
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employee = createCookRequest();

        // when
        EmployeeDTO employeeCreated = employeeService.save(employee);

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() throws EmployeeNotFoundException {
        // when
        employeeService.deleteById(2L);
        List<EmployeeDTO> employees = employeeService.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllEmployees(){
        // when
        List<EmployeeDTO> employeesReturned = employeeService.findAll();

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployees(employeesReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneEmployee(){
        // when
        EmployeeDTO employeeReturned = employeeService.findById(1L).orElse(null);

        // then
        EmployeeDTOAssertions.checkAssertionsForEmployee(employeeReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNamePass() throws JobNotFoundException {
        // when
        List<EmployeeDTO> employeesReturned = employeeService.findByJob("Cook");

        // then
        EmployeeDTOAssertions.checkAssertionsForCooks(employeesReturned);
    }

/*    private void checkAssertionsForEmployee(EmployeeDTO employee){
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
    }*/
}
