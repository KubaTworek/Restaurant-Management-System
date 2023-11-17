package pl.jakubtworek.employee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeFactoryTest {
    @Test
    void shouldCreateEmployee() {
        // Given
        final var firstName = "John";
        final var lastName = "Doe";
        final var job = "COOK";

        // When
        final var employee = EmployeeFactory.createEmployee(firstName, lastName, job);

        // Then
        final var result = employee.getSnapshot();
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(job, result.getJob().name());
    }
}
