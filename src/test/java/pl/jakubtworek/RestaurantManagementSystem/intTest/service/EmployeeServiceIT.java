package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/schema.sql")
public class EmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllEmployees(){
        // when
        List<Employee> employees = employeeService.findAll();

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
    public void shouldReturnOneEmployee(){
        // when
        Optional<Employee> employee = employeeService.findById(1L);

        // then
        assertEquals("John", employee.get().getFirstName());
        assertEquals("Smith", employee.get().getLastName());
        assertEquals("Cook", employee.get().getJob().getName());
        assertEquals(2, employee.get().getOrders().size());
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnHigherSizeOfList_whenCreateOne(){
        Job job = spy(new Job(1L,"Cook",List.of()));
        Employee employee = spy(new Employee(444L, anyString(), anyString(), job, anyList()));
        employeeRepository.save(employee);
        List<Employee> employees = employeeRepository.findAll();

        // then
        assertEquals(4, employees.size());
    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne(){
        // when
        employeeService.deleteById(2L);
        List<Employee> employees = employeeService.findAll();

        // then
        assertEquals(2, employees.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnEmployees_whenJobNamePass(){
        // when
        List<Employee> employees = employeeService.findByJob("Cook");

        // then
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Smith", employees.get(0).getLastName());
        assertEquals("Cook", employees.get(0).getJob().getName());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnEmptyList_whenWrongJobNamePass(){
        // when
        List<Employee> employees = employeeService.findByJob("Random");

        // then
        assertEquals(0, employees.size());
    }
}
