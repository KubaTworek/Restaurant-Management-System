package pl.jakubtworek.employee;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.dto.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldPostEmployee() {
        // given
        final var request = new EmployeeRequest("John", "Doe", "COOK");

        // when
        final var response = postEmployee(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void shouldGetAllEmployees() {
        // given
        final var employeeId = postEmployee(
                new EmployeeRequest("John", "Doe", "COOK")
        ).getBody().getId();
        postEmployee(
                new EmployeeRequest("Jane", "Smith", "WAITER")
        );

        // when
        deleteEmployeeById(employeeId);
        final var response = getEmployees();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var employees = List.of(response.getBody());
        assertEquals(1, employees.size());
    }

    @Test
    @DirtiesContext
    void shouldDeleteEmployee() {
        // given
        final var createdId = postEmployee(
                new EmployeeRequest("John", "Doe", "COOK")
        ).getBody().getId();

        // when
        final var firstDelete = deleteEmployeeById(createdId);
        final var secondDelete = deleteEmployeeById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, firstDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, secondDelete.getStatusCode());
        final var deletedOrder = getEmployeeById(createdId);
        assertEquals(Status.INACTIVE, deletedOrder.getBody().getStatus());
    }

    @Test
    @DirtiesContext
    void shouldGetEmployeeById() {
        // given
        final var createdId = postEmployee(
                new EmployeeRequest("John", "Doe", "COOK")
        ).getBody().getId();

        // when
        final var retrievedResponse = getEmployeeById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("John", retrievedResponse.getBody().getFirstName());
        assertEquals("Doe", retrievedResponse.getBody().getLastName());
        assertEquals(Job.COOK, retrievedResponse.getBody().getJob());
    }

    @Test
    @DirtiesContext
    void shouldGetEmployeeByJob() {
        // given
        postEmployee(
                new EmployeeRequest("John", "Doe", "COOK")
        );
        postEmployee(
                new EmployeeRequest("Jane", "Smith", "WAITER")
        );

        // when
        final var response = getEmployeeByJob("COOK");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var employees = List.of(response.getBody());
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Doe", employees.get(0).getLastName());
        assertEquals(Job.COOK, employees.get(0).getJob());
    }
}
