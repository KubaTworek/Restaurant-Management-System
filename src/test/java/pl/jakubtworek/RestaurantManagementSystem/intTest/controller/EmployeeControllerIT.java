package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.GetEmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private JobRepository jobRepository;
    @Mock
    private EmployeeFactory employeeFactory;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        mock(EmployeeRepository.class);
        mock(JobRepository.class);

        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                jobRepository,
                employeeFactory
        );
        employeeController = new EmployeeController(
                employeeService
        );
        when(employeeRepository.findAll()).thenReturn(List.of(spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()))));
        when(employeeRepository.findById(eq(1L))).thenReturn(Optional.of(spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()))));
        when(employeeRepository.findByJob(any(Job.class))).thenReturn(List.of(spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of()))));
        when(jobRepository.findByName(eq("Cook"))).thenReturn(Optional.of(spy(new Job(1L,"Cook",List.of()))));
    }


    @Test
    void shouldReturnAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id/1"))
                .andExpect(status().is(200))
                .andReturn();
        EmployeeDTO employee = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeDTO.class);

        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("John");
        assertThat(employee.getLastName()).isEqualTo("Smith");
        assertThat(employee.getJob().getId()).isNotNull();
        assertThat(employee.getJob().getName()).isEqualTo("Cook");
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id/4"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are employees in restaurant with that id: 4");
    }

    @Test
    @Sql(statements = "INSERT INTO `job` VALUES (1,'Cook'), (2,'Waiter'), (3,'DeliveryMan')")
    void shouldReturnCreatedEmployee() throws Exception {
        GetEmployeeDTO employee = new GetEmployeeDTO(4L, "James", "Smith", "Cook");

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();
        EmployeeDTO employeeReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeDTO.class);

        assertThat(employeeReturned).isNotNull();
        assertThat(employeeReturned.getId()).isNotNull();
        assertThat(employeeReturned.getFirstName()).isEqualTo("James");
        assertThat(employeeReturned.getLastName()).isEqualTo("Smith");
        assertThat(employeeReturned.getJob().getId()).isNotNull();
        assertThat(employeeReturned.getJob().getName()).isEqualTo("Cook");
    }

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/employees/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted employee has id: 1");
    }

    @Test
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/job/Cook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<EmployeeDTO> employeesReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EmployeeDTO>>(){});

        assertThat(employeesReturned).isNotNull();
        assertThat(employeesReturned.get(0)).isNotNull();
        assertThat(employeesReturned.get(0).getFirstName()).isEqualTo("John");
        assertThat(employeesReturned.get(0).getLastName()).isEqualTo("Smith");
        assertThat(employeesReturned.get(0).getJob().getId()).isNotNull();
        assertThat(employeesReturned.get(0).getJob().getName()).isEqualTo("Cook");
    }

    @Test
    void shouldReturnResponse_whenWrongJobNameIsPassed() throws Exception {
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