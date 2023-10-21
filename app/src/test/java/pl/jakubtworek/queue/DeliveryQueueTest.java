package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeliveryQueueTest {
    private DeliveryQueue deliveryQueue;
    private SimpleEmployee delivery1;
    private SimpleEmployee delivery2;

    @BeforeEach
    void setUp() {
        deliveryQueue = new DeliveryQueue();
        delivery1 = new SimpleEmployee(1L, "John", "Doe", Job.DELIVERY);
        delivery2 = new SimpleEmployee(2L, "Bob", "Burton", Job.DELIVERY);
    }

    @Test
    void shouldAddDeliveryToQueue() {
        // given
        final var observer = mock(Observer.class);
        deliveryQueue.registerObserver(observer);

        // when
        deliveryQueue.add(delivery1);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, deliveryQueue.size());
    }

    @Test
    void shouldGetDeliveryFromQueue() {
        // given
        deliveryQueue.add(delivery1);
        deliveryQueue.add(delivery2);

        // when
        final SimpleEmployee removedDeliveryPerson = deliveryQueue.get();

        // then
        assertEquals(delivery1, removedDeliveryPerson);
        assertEquals(1, deliveryQueue.size());
    }

    @Test
    void shouldReturnQueueSize() {
        // given
        deliveryQueue.add(delivery1);
        deliveryQueue.add(delivery2);

        // when
        final int queueSize = deliveryQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}

