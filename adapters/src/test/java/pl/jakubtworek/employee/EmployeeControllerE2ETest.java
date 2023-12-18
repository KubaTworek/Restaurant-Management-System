package pl.jakubtworek.employee;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.vo.Job;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldPostEmployee() {
        // given
        final var request = new EmployeeRequest("John", "Doe", "COOK");

        // when
        final var response = postEmployee(request);

        // then
        assertEmployeeResponse(response);
    }

    @Test
    @DirtiesContext
    void shouldDeactivateEmployeeById() {
        // given
        final var created = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));

        // when
        final var firstDelete = deactivateEmployeeById(created.getId());
        final var secondDelete = deactivateEmployeeById(created.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT, firstDelete.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, secondDelete.getStatusCode());
        final var deletedOrder = getEmployeeById(created.getId());
        assertEquals(Status.INACTIVE, deletedOrder.getStatus());
    }

    @Test
    @DirtiesContext
    void shouldGetAllEmployees() {
        // given
        postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));

        // when
        final var response = getEmployees();

        // then
        assertEquals(2, response.size());
    }

    @Test
    @DirtiesContext
    void shouldGetEmployeeById() {
        // given
        final var created = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));

        // when
        final var response = getEmployeeById(created.getId());

        // then
        assertEmployeeResponse(response);
    }

    private void assertEmployeeResponse(EmployeeDto response) {
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(Job.COOK, response.getJob());
        assertEquals(Status.ACTIVE, response.getStatus());
    }
}
