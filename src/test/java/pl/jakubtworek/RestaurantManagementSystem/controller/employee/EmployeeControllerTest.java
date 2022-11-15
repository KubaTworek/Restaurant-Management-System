package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        mock(EmployeeService.class);

        employeeController = new EmployeeController(
                employeeService
        );
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        // given
        List<Employee> expectedEmployees = createEmployees();

        // when
        when(employeeService.findAll()).thenReturn(expectedEmployees);

        MvcResult mvcResult = mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
        List<EmployeeResponse> employeesReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());

        assertEquals("James", employeesReturned.get(1).getFirstName());
        assertEquals("Patel", employeesReturned.get(1).getLastName());
        assertEquals("Waiter", employeesReturned.get(1).getJob().getName());

        assertEquals("Ann", employeesReturned.get(2).getFirstName());
        assertEquals("Mary", employeesReturned.get(2).getLastName());
        assertEquals("DeliveryMan", employeesReturned.get(2).getJob().getName());
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        // given
        Optional<Employee> expectedEmployee = createEmployee();

        // when
        when(employeeService.findById(eq(1L))).thenReturn(expectedEmployee);

        MvcResult mvcResult = mockMvc.perform(get("/employees/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().is(200))
                .andReturn();
        EmployeeResponse employeeReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeResponse.class);

        // then
        assertEquals("John", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingEmployee() throws Exception {
        // when
        when(employeeService.checkIfEmployeeIsNull(eq(4L))).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(get("/employees/id")
                        .param("id", String.valueOf(4L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no employees in restaurant with that id: 4", response.getMessage());
    }

/*    @Test
    void shouldReturnCreatedEmployee() throws Exception {
        // given
        Employee returnedEmployee = new Employee(0L, "James", "Smith", new Job(1L, "Cook", List.of()), List.of());
        EmployeeRequest employee = new EmployeeRequest(0L, "James", "Smith", "Cook");
        EmployeeResponse expectedEmployee = new EmployeeResponse(0L, "James", "Smith", new JobDTO(1L, "Cook"));

        // when
        when(employeeService.save(employee)).thenReturn(returnedEmployee);

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();
        EmployeeResponse employeeReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeResponse.class);

        // then
        assertEquals("John", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(delete("/employees/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted employee has id: 1", response);
    }

    @Test
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        // given
        List<Employee> expectedCooks = createCooks();

        // when
        when(employeeService.findByJob(eq("Cook"))).thenReturn(expectedCooks);

        MvcResult mvcResult = mockMvc.perform(get("/employees/job")
                        .param("job", "Cook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<EmployeeResponse> employeesReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());
    }

    @Test
    void shouldReturnResponse_whenWrongJobNameIsPassed() throws Exception {
        // when
        when(employeeService.checkIfJobIsNull(eq("something"))).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(get("/employees/job")
                        .param("job", "something")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no job like that in restaurant with that name: something", response.getMessage());
    }
}
