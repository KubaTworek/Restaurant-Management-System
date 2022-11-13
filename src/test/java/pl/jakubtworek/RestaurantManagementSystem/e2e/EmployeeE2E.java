package pl.jakubtworek.RestaurantManagementSystem.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.GetEmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/schema.sql")
public class EmployeeE2E {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllEmployees() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployeeById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id")
                        .param("id", String.valueOf(1L)))
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnErrorResponse_whenAskedForNonExistingEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id")
                        .param("id", String.valueOf(4L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no employees in restaurant with that id: 4");
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/employees/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted employee has id: 1");
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenJobNameIsPassed() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/job")
                        .param("job", "Cook")
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponse_whenWrongJobNameIsPassed() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/job")
                        .param("job", "something")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no job like that in restaurant with that name: something");
    }
}