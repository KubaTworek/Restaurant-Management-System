package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmployeeQueueFacadeTest {
    @Mock
    private CooksQueue cooksQueue;
    @Mock
    private WaiterQueue waiterQueue;
    @Mock
    private DeliveryQueue deliveryQueue;

    private EmployeeQueueFacade employeeQueueFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeQueueFacade = new EmployeeQueueFacade(cooksQueue, waiterQueue, deliveryQueue);
    }

    @Test
    void shouldAddEmployeeToProperQueue() {
        // given
        final var cook = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var waiter = new SimpleEmployee(2L, "Alice", "Patel",  Job.WAITER);
        final var deliveryPerson = new SimpleEmployee(3L, "Bob", "Burton", Job.DELIVERY);

        // when
        employeeQueueFacade.addToQueue(cook);
        employeeQueueFacade.addToQueue(waiter);
        employeeQueueFacade.addToQueue(deliveryPerson);

        // then
        verify(cooksQueue, times(1)).add(cook);
        verify(waiterQueue, times(1)).add(waiter);
        verify(deliveryQueue, times(1)).add(deliveryPerson);
    }
}
