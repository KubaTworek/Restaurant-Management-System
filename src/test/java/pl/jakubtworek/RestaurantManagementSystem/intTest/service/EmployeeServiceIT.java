package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@Transactional
class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllEmployees(){
        // when
        List<EmployeeDTO> employees = employeeService.findAll();

        // then
        assertEquals(3, employees.size());

        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals("Cook", employees.get(0).getJob().getName());
        assertEquals(2, employees.get(0).getOrders().size());

        assertEquals("James", employees.get(1).getFirstName());
        assertEquals("Patel", employees.get(1).getLastName());
        assertEquals("Waiter", employees.get(1).getJob().getName());
        assertEquals(0, employees.get(1).getOrders().size());

        assertEquals("Ann", employees.get(2).getFirstName());
        assertEquals("Mary", employees.get(2).getLastName());
        assertEquals("DeliveryMan", employees.get(2).getJob().getName());
        assertEquals(0, employees.get(2).getOrders().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneEmployee(){
        // when
        Optional<EmployeeDTO> employee = employeeService.findById(1L);

        // then
        assertEquals("John", employee.get().getFirstName());
        assertEquals("Smith", employee.get().getLastName());
        assertEquals("Cook", employee.get().getJob().getName());
        assertEquals(2, employee.get().getOrders().size());
    }

    @Test
    @Sql(statements = "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')")
    void shouldReturnCreatedEmployee() throws JobNotFoundException {
        // given
        EmployeeRequest employee = createCookRequest();
        JobDTO job = createJobCook().convertEntityToDTO();

        // when
        EmployeeDTO employeeReturned = employeeService.save(employee, job);

        // then
        assertEquals("James", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne(){
        // when
        employeeService.deleteById(2L);
        List<EmployeeDTO> employees = employeeService.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNamePass(){
        // given
        Job job = createJobCook();

        // when
        List<EmployeeDTO> employees = employeeService.findByJob(job);

        // then
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals("Cook", employees.get(0).getJob().getName());
    }
}
