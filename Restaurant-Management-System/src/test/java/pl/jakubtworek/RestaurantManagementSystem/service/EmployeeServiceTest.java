package pl.jakubtworek.RestaurantManagementSystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Job(id, name)" + "VALUES (1,'Cook')");
        jdbc.execute("INSERT INTO Job(id, name)" + "VALUES (2,'Waiter')");
        jdbc.execute("INSERT INTO Job(id, name)" + "VALUES (3,'DeliveryMan')");
        jdbc.execute("INSERT INTO Employee(id, first_name, last_name, job_id)" + "VALUES (1,'John','Smith',1)");
        jdbc.execute("INSERT INTO Employee(id, first_name, last_name, job_id)" + "VALUES (2,'James','Patel',2)");
        jdbc.execute("INSERT INTO Employee(id, first_name, last_name, job_id)" + "VALUES (3,'Ann','Mary',3)");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void findAllEmployeesTest(){
        Iterable<Employee> iterableEmployees = employeeService.findAll();

        List<Employee> employees = new ArrayList<>();

        for(Employee employee : iterableEmployees) {
            employees.add(employee);
        }

        assertEquals(3,employees.size());
    }

    @Test
    public void findByIdEmployeeTest(){
        Employee employee = employeeService.findById(3);

        assertEquals("Ann",employee.getFirstName());
    }

    @Test
    public void saveEmployeeTest(){
        Employee employee = new Employee();
        employee.setFirstName("Adam");
        employee.setLastName("Johnson");
        employeeService.save(employee);

        Iterable<Employee> iterableEmployees = employeeService.findAll();

        List<Employee> employees = new ArrayList<>();

        for(Employee tempEmployee : iterableEmployees) {
            employees.add(tempEmployee);
        }

        assertEquals(4,employees.size());
    }

    @Test
    public void deleteByIdEmployeeTest(){
        employeeService.deleteById(3);

        Iterable<Employee> iterableEmployees = employeeService.findAll();

        List<Employee> employees = new ArrayList<>();

        for(Employee tempEmployee : iterableEmployees) {
            employees.add(tempEmployee);
        }

        assertEquals(2,employees.size());
    }

    @Test
    public void findByNameJobTest(){
        Iterable<Employee> iterableEmployees = employeeService.findByJob("Cook");

        List<Employee> employees = new ArrayList<>();

        for(Employee tempEmployee : iterableEmployees) {
            employees.add(tempEmployee);
        }

        assertEquals(1,employees.size());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Employee");
        jdbc.execute("DELETE FROM Job");
    }

}
