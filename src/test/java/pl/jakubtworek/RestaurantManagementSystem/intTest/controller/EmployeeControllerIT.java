package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

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

    @Test
    void shouldReturnAllEmployees() throws Exception {
        // given
        List<Employee> expectedEmployees = createEmployees();

        // when
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

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
        when(employeeRepository.findById(eq(1L))).thenReturn(expectedEmployee);

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
        EmployeeRequest employee = createCookRequest();
        Optional<Employee> expectedEmployee = createEmployee();
        Optional<Job> expectedJob = createCook();

        // when
        when(jobRepository.findByName(eq("Cook"))).thenReturn(expectedJob);
        when(employeeRepository.save(any())).thenReturn(expectedEmployee);

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();
        EmployeeResponse employeeReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeResponse.class);

        // then
        assertEquals("James", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<Employee> expectedEmployee = createEmployee();

        // when
        when(employeeRepository.findById(eq(1L))).thenReturn(expectedEmployee);

        MvcResult mvcResult = mockMvc.perform(delete("/employees/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        EmployeeResponse employeeDeleted = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeResponse.class);

        // then
        assertEquals("John", employeeDeleted.getFirstName());
        assertEquals("Smith", employeeDeleted.getLastName());
        assertEquals("Cook", employeeDeleted.getJob().getName());
    }

    @Test
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        // given
        Optional<Job> expectedJob = createCook();
        Optional<Employee> expectedCooks = createCooks();

        // when
        when(jobRepository.findByName(eq("Cook"))).thenReturn(expectedJob);
        when(employeeRepository.findByJob(any(Job.class))).thenReturn(expectedCooks);

        MvcResult mvcResult = mockMvc.perform(get("/employees/job")
                        .param("job", "Cook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<EmployeeResponse> employeesReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        // then
        assertEquals("John", employeesReturned.get(0).getFirstName());
        assertEquals("Smith", employeesReturned.get(0).getLastName());
        assertEquals("Cook", employeesReturned.get(0).getJob().getName());
    }

    @Test
    void shouldReturnResponse_whenWrongJobNameIsPassed() throws Exception {
        // when
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