package pl.jakubtworek.RestaurantManagementSystem.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class EmployeeRestControllerTest {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Employee employee;

    @Autowired
    private Job job;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

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
    public void getEmployeesHttpRequest() throws Exception {
        employee.setFirstName("Adam");
        employee.setLastName("Johnson");
        entityManager.persist(employee);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void getEmployeeByIdHttpRequest() throws Exception {
        Employee employee = employeeService.findById(1);

        assertNotNull(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    public void saveEmployeeHttpRequest() throws Exception {
        employee.setFirstName("Adam");
        employee.setLastName("Johnson");

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void deleteEmployeeHttpRequest() throws Exception {
        assertNotNull(employeeService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getEmployeeByJobHttpRequest() throws Exception {
        List<Employee> employees = employeeService.findByJob("Cook");

        assertNotNull(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{jobName}", "Cook"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }




    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Employee");
        jdbc.execute("DELETE FROM Job");

    }
}
