package pl.jakubtworek.employee;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.vo.Job;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeTest {
    @Test
    void shouldRestoreEmployeeFromSnapshot() {
        // given
        final var snapshot = new EmployeeSnapshot(1L, "John", "Doe", Job.COOK, Status.ACTIVE, new HashSet<>());

        // when
        final var employee = Employee.restore(snapshot);

        // then
        final var result = employee.getSnapshot();
        assertEquals(snapshot.getId(), result.getId());
        assertEquals(snapshot.getFirstName(), result.getFirstName());
        assertEquals(snapshot.getLastName(), result.getLastName());
        assertEquals(snapshot.getJob(), result.getJob());
        assertEquals(snapshot.getStatus(), result.getStatus());
        assertEquals(snapshot.getOrders(), result.getOrders());
    }

    @Test
    void shouldUpdateEmployeeInfo() {
        // given
        final var employee = new Employee();

        // when
        employee.updateInfo("Jane", "Doe", "COOK");

        // then
        final var result = employee.getSnapshot();
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(Job.COOK, result.getJob());
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    void shouldUpdateEmployeeInfoWithInvalidJob() {
        // given
        final var employee = new Employee();

        // when & then
        assertThrows(IllegalStateException.class, () ->
                employee.updateInfo("Jane", "Doe", "INVALID_JOB_TYPE")
        );
    }
}
