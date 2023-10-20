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
    private SimpleEmployee deliveryPerson1;
    private SimpleEmployee deliveryPerson2;

    @BeforeEach
    void setUp() {
        deliveryQueue = new DeliveryQueue();
        deliveryPerson1 = new SimpleEmployee(1L, "John", "Doe", Job.DELIVERY);
        deliveryPerson2 = new SimpleEmployee(2L, "Bob", "Burton", Job.DELIVERY);
    }

    @Test
    void testAdd_AddDeliveryPersonToQueue() {
        // given
        Observer observer = mock(Observer.class);
        deliveryQueue.registerObserver(observer);

        // when
        deliveryQueue.add(deliveryPerson1);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, deliveryQueue.size());
    }

    @Test
    void testGet_GetDeliveryPersonFromQueue() {
        // given
        deliveryQueue.add(deliveryPerson1);
        deliveryQueue.add(deliveryPerson2);

        // when
        SimpleEmployee removedDeliveryPerson = deliveryQueue.get();

        // then
        assertEquals(deliveryPerson1, removedDeliveryPerson);
        assertEquals(1, deliveryQueue.size());
    }

    @Test
    void testSize_ReturnQueueSize() {
        // given
        deliveryQueue.add(deliveryPerson1);
        deliveryQueue.add(deliveryPerson2);

        // when
        int queueSize = deliveryQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}

