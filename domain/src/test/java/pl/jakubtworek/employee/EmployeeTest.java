package pl.jakubtworek.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.Job;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeTest {
    @Mock
    private EmployeeRepository repository;
    @Mock
    private DomainEventPublisher publisher;
    @Captor
    private ArgumentCaptor<EmployeeEvent> event;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = Employee.restore(new EmployeeSnapshot(1L, "John", "Doe", Job.COOK, Status.ACTIVE, new HashSet<>()));
        employee.setDependencies(
                publisher,
                repository
        );

        when(repository.save(employee)).thenReturn(employee);
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
    }

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
    void shouldCreateEmployee() {
        // when
        employee.from("Jane", "Doe", "COOK");

        // then
        final var result = employee.getSnapshot();
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(Job.COOK, result.getJob());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(publisher).publish(event.capture());
        final var eventCaptured = event.getValue();
        assertEquals(result.getId(), eventCaptured.getEmployeeId());
        assertNull(eventCaptured.getOrderId());
        assertEquals(result.getJob(), eventCaptured.getJob());
    }

    @Test
    void shouldDeactivateEmployee() {
        // when
        employee.deactivate(1L);

        // then
        assertEquals(Status.INACTIVE, employee.getSnapshot().getStatus());
    }

    @Test
    void shouldThrowException_whenDeactivatingInactiveEmployee() {
        // given
        employee.deactivate(1L);

        // when & then
        assertThrows(IllegalStateException.class,
                () -> employee.deactivate(1L));
    }

    @Test
    void shouldThrowException_whenDeactivatingNonExistingEmployee() {
        // when & then
        assertThrows(IllegalStateException.class,
                () -> employee.deactivate(2L));
    }

    @Test
    void shouldThrowException_whenCreateEmployeeJobIsInvalid() {
        // when & then
        assertThrows(IllegalStateException.class, () ->
                employee.from("Jane", "Doe", "INVALID_JOB_TYPE")
        );
    }
}
