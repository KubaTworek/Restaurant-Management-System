package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WaiterQueueTest {
    @Mock
    private Observer observer;

    private WaiterQueue waiterQueue;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        waiterQueue = new WaiterQueue();
        waiterQueue.registerObserver(observer);
    }

    @Test
    void testAdd_AddWaiterToQueue() {
        // given
        SimpleEmployee waiter = new SimpleEmployee(1L, "John", "Doe", Job.WAITER);

        // when
        waiterQueue.add(waiter);

        // then
        verify(observer, times(1)).update();
    }

    @Test
    void testGet_GetWaiterFromQueue() {
        // given
        SimpleEmployee waiter1 = new SimpleEmployee(1L, "John", "Doe", Job.WAITER);
        SimpleEmployee waiter2 = new SimpleEmployee(1L, "Bob", "Burton", Job.WAITER);

        // when
        waiterQueue.add(waiter1);
        waiterQueue.add(waiter2);
        SimpleEmployee removedWaiter = waiterQueue.get();

        // then
        verify(observer, times(2)).update();
        assertEquals(waiter1, removedWaiter);
    }

    @Test
    void testSize_ReturnQueueSize() {
        // given
        SimpleEmployee waiter1 = new SimpleEmployee(1L, "John", "Doe", Job.WAITER);
        SimpleEmployee waiter2 = new SimpleEmployee(1L, "Bob", "Burton", Job.WAITER);

        waiterQueue.add(waiter1);
        waiterQueue.add(waiter2);

        // when
        int queueSize = waiterQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}

