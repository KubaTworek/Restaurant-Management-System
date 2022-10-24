package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbc;

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
    void postIncorrectHeader() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/employees/"))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(406);
        assertThat(response.getMessage()).isEqualTo("Accept: application/json");
    }

    @Test
    void getEmployees() throws Exception {
        mockMvc.perform(get("/employees/"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getEmployeeById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id/1"))
                .andExpect(status().is(200))
                .andReturn();
        Employee employee = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Employee.class);

        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getFirstName()).isEqualTo("John");
        assertThat(employee.getLastName()).isEqualTo("Smith");
        assertThat(employee.getJob().getId()).isEqualTo(1L);
        assertThat(employee.getJob().getName()).isEqualTo("Cook");
    }

    @Test
    void getEmployeeByWrongId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id/4"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are employees in restaurant with that id: 4");
    }

    @Test
    void saveEmployee() throws Exception {
        Job job = new Job(4L, "Cleaner", null);
        Employee employee = new Employee(4L, "James", "Smith", job, null);

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();
        Employee employeeGet = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Employee.class);

        assertThat(employeeGet).isNotNull();
        assertThat(employeeGet.getId()).isEqualTo(0L);
        assertThat(employeeGet.getFirstName()).isEqualTo("James");
        assertThat(employeeGet.getLastName()).isEqualTo("Smith");
        assertThat(employeeGet.getJob().getId()).isEqualTo(4L);
        assertThat(employeeGet.getJob().getName()).isEqualTo("Cleaner");
    }

    @Test
    void deleteEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted employee has id: 1");
    }

    @Test
    void getEmployeeByJobName() throws Exception {
        mockMvc.perform(get("/employees/job/cook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getEmployeeByWrongJobName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/job/something")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no job like that in restaurant with that name: something");
    }
}