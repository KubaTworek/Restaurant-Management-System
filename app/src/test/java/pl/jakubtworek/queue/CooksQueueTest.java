package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CooksQueueTest {
    private CooksQueue cooksQueue;
    private SimpleEmployee cook1;
    private SimpleEmployee cook2;

    @BeforeEach
    void setUp() {
        cooksQueue = new CooksQueue();
        cook1 = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        cook2 = new SimpleEmployee(2L, "Bob", "Burton", Job.COOK);
    }

    @Test
    void shouldAddCookToQueue() {
        // given
        final var observer = mock(Observer.class);
        cooksQueue.registerObserver(observer);

        // when
        cooksQueue.add(cook1);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, cooksQueue.size());
    }

    @Test
    void shouldGetCookFromQueue() {
        // given
        cooksQueue.add(cook1);
        cooksQueue.add(cook2);

        // when
        final SimpleEmployee removedCook = cooksQueue.get();

        // then
        assertEquals(cook1, removedCook);
        assertEquals(1, cooksQueue.size());
    }

    @Test
    void shouldReturnQueueSize() {
        // given
        cooksQueue.add(cook1);
        cooksQueue.add(cook2);

        // when
        final int queueSize = cooksQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}
