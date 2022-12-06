package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;

@SpringBootTest
class EmployeeControllerIT {

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private JobRepository jobRepository;

    @Test
    void shouldReturnAllEmployees() {
        // given
        List<Employee> expectedEmployees = createEmployees();

        // when
        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        List<EmployeeResponse> employeesReturned = employeeController.getEmployees().getBody();

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
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(1L)).thenReturn(expectedEmployee);

        EmployeeResponse employeeReturned = employeeController.getEmployeeById(1L).getBody();

        // then
        assertEquals("John", employeeReturned.getFirstName());
        assertEquals("Smith", employeeReturned.getLastName());
        assertEquals("Cook", employeeReturned.getJob().getName());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingEmployee() {
        // when
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> employeeController.getEmployeeById(4L));

        // then
        assertEquals("There are no employees in restaurant with that id: 4", exception.getMessage());
    }

/*    @Test
    void shouldReturnCreatedEmployee() throws Exception {

    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedEmployee() throws Exception {
        // given
        Optional<Employee> expectedEmployee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(eq(1L))).thenReturn(expectedEmployee);

        EmployeeResponse employeeDeleted = employeeController.deleteEmployee(1L).getBody();

        // then
        assertEquals("John", employeeDeleted.getFirstName());
        assertEquals("Smith", employeeDeleted.getLastName());
        assertEquals("Cook", employeeDeleted.getJob().getName());
    }

/*    @Test
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
    }*/

    @Test
    void shouldReturnResponse_whenWrongJobNameIsPassed() {
        // when
        Exception exception = assertThrows(JobNotFoundException.class, () -> employeeController.getEmployeeByJobName("something"));

        // then
        assertEquals("There are no job in restaurant with that name: something", exception.getMessage());
    }
}