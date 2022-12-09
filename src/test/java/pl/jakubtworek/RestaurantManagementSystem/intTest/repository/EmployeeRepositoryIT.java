package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createJobCook;

@SpringBootTest
@Transactional
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllEmployees(){
        // when
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertEquals(3, employees.size());
        assertEquals(1, employees.get(0).getId());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals(1, employees.get(0).getJob().getId());
        assertEquals("Cook", employees.get(0).getJob().getName());
        assertEquals(2, employees.get(0).getOrders().size());
        assertEquals(2, employees.get(1).getId());
        assertEquals("James", employees.get(1).getFirstName());
        assertEquals("Patel", employees.get(1).getLastName());
        assertEquals(2, employees.get(1).getJob().getId());
        assertEquals("Waiter", employees.get(1).getJob().getName());
        assertEquals(0, employees.get(1).getOrders().size());
        assertEquals(3, employees.get(2).getId());
        assertEquals("Ann", employees.get(2).getFirstName());
        assertEquals("Mary", employees.get(2).getLastName());
        assertEquals(3, employees.get(2).getJob().getId());
        assertEquals("DeliveryMan", employees.get(2).getJob().getName());
        assertEquals(0, employees.get(2).getOrders().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneEmployee(){
        // when
        Optional<Employee> employee = employeeRepository.findById(1L);

        // then
        assertNotNull(employee.get());
        assertEquals(1, employee.get().getId());
        assertEquals("John", employee.get().getFirstName());
        assertEquals("Smith", employee.get().getLastName());
        assertEquals(1, employee.get().getJob().getId());
        assertEquals("Cook", employee.get().getJob().getName());
        assertEquals(2, employee.get().getOrders().size());
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnCreatedEmployee(){

    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne(){
        // when
        employeeRepository.deleteById(2L);
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNamePass(){
        // given
        Job job = createJobCook();

        // when
        List<Employee> employees = employeeRepository.findByJob(job);

        // then
        assertEquals(1, employees.size());
        assertEquals(1, employees.get(0).getId());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals(1, employees.get(0).getJob().getId());
        assertEquals("Cook", employees.get(0).getJob().getName());
    }
}
