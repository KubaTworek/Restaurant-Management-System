package pl.jakubtworek.RestaurantManagementSystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CooksQueue cooksQueue;
    @Autowired
    private WaiterQueue waiterQueue;
    @Autowired
    private DeliveryQueue deliveryQueue;

    @Value("${sql.script.create.employees}")
    private String sqlAddEmployees;

    @Value("${sql.script.create.jobs}")
    private String sqlAddJobs;

    @Value("${sql.script.delete.employees}")
    private String sqlDeleteEmployees;

    @Value("${sql.script.delete.jobs}")
    private String sqlDeleteJobs;
    @BeforeEach
    void setup() {
        jdbc.execute(sqlAddJobs);
        jdbc.execute(sqlAddEmployees);
    }

    @AfterEach
    void delete() {
        jdbc.execute(sqlDeleteEmployees);
        jdbc.execute(sqlDeleteJobs);
    }

    @Test
    void findAll() {
        List<Employee> employees = employeeService.findAll();
        assertEquals(3,employees.size());
    }

    @Test
    void findById() {
        Optional<Employee> employee = employeeService.findById(1L);
        assertEquals("John", employee.get().getFirstName());
        assertEquals("Smith", employee.get().getLastName());
    }

    @Test
    void save() {
        Employee employeeTest = new Employee(4L,"Adam","Kowalski", employeeService.findJobByName("Cook").get(), null);
        employeeService.save(employeeTest);
        List<Employee> employees = employeeService.findAll();
        assertEquals(4,employees.size());
    }

    @Test
    void deleteById() {
        Optional<Employee> deletedEmployee= employeeRepository.findById(1L);
        assertTrue(deletedEmployee.isPresent());
        employeeService.deleteById(1L);
        deletedEmployee = employeeRepository.findById(1L);
        assertFalse(deletedEmployee.isPresent());
    }

    @Test
    void findByJob() {
        List<Employee> cooks = employeeService.findByJob("Cook");
        List<Employee> waiters = employeeService.findByJob("Waiter");
        List<Employee> deliveries = employeeService.findByJob("DeliveryMan");
        assertEquals(1,cooks.size());
        assertEquals(1,waiters.size());
        assertEquals(1,deliveries.size());
    }

    @Test
    void findJobByName() {
        Optional<Job> job = employeeService.findJobByName("Cook");
        assertEquals(1,job.get().getId());
    }

    @Test
    void addCooksToKitchen() {
        assertEquals(0,cooksQueue.size());
        employeeService.addCooksToKitchen();
        assertEquals(1,cooksQueue.size());
    }

    @Test
    void addWaitersToKitchen() {
        assertEquals(0,waiterQueue.size());
        employeeService.addWaitersToKitchen();
        assertEquals(1,waiterQueue.size());
    }

    @Test
    void addDeliveriesToKitchen() {
        assertEquals(0,deliveryQueue.size());
        employeeService.addDeliveriesToKitchen();
        assertEquals(1,deliveryQueue.size());
    }

    @Test
    void checkIfEmployeeIsNull() {
        Optional<Employee> employeeOne = employeeService.findById(1L);
        Optional<Employee> employeeTwo = employeeService.findById(4L);

        assertTrue(employeeOne.isPresent());
        assertFalse(employeeTwo.isPresent());
    }

    @Test
    void checkIfJobIsNull() {
        Optional<Job> jobOne = employeeService.findJobByName("Cook");
        Optional<Job> jobTwo = employeeService.findJobByName("Cleaner");

        assertTrue(jobOne.isPresent());
        assertFalse(jobTwo.isPresent());
    }
}