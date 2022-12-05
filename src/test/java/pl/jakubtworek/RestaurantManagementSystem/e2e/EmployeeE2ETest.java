package pl.jakubtworek.RestaurantManagementSystem.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllEmployees() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployeeById() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
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
    @Sql(statements = "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')")
    void shouldReturnCreatedEmployee() throws Exception {
        // given
        EmployeeRequest employee = createCookRequest();

        // when
        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andReturn();
        EmployeeResponse employeeReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EmployeeResponse.class);

        // when
        assertEquals("James", employeeReturned.getFirstName());
        assertEquals("Morgan", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
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