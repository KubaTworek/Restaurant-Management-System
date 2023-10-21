package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WaiterQueueTest {
    private WaiterQueue waiterQueue;
    private SimpleEmployee waiter1;
    private SimpleEmployee waiter2;

    @BeforeEach
    void setUp() {
        waiterQueue = new WaiterQueue();
        waiter1 = new SimpleEmployee(1L, "John", "Doe", Job.WAITER);
        waiter2 = new SimpleEmployee(2L, "Bob", "Burton", Job.WAITER);
    }

    @Test
    void shouldAddWaiterToQueue() {
        // given
        final var observer = mock(Observer.class);
        waiterQueue.registerObserver(observer);

        // when
        waiterQueue.add(waiter1);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, waiterQueue.size());
    }

    @Test
    void shouldGetWaiterFromQueue() {
        // given
        waiterQueue.add(waiter1);
        waiterQueue.add(waiter2);

        // when
        final SimpleEmployee removedDeliveryPerson = waiterQueue.get();

        // then
        assertEquals(waiter1, removedDeliveryPerson);
        assertEquals(1, waiterQueue.size());
    }

    @Test
    void shouldReturnQueueSize() {
        // given
        waiterQueue.add(waiter1);
        waiterQueue.add(waiter2);

        // when
        final int queueSize = waiterQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}
