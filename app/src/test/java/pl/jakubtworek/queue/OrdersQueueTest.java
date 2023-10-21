package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OrdersQueueTest {
    private OrdersQueue ordersQueue;
    private SimpleOrder onSiteOrder;
    private SimpleOrder deliveryOrder;
    private SimpleOrder takeAwayOrder;

    @BeforeEach
    void setUp() {
        ordersQueue = new OrdersQueue();
        onSiteOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        deliveryOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        takeAwayOrder = new SimpleOrder(3L, 400, ZonedDateTime.now(), null, TypeOfOrder.TAKE_AWAY);
    }

    @Test
    void shouldAddOrderToQueue() {
        // given
        final var observer = mock(Observer.class);
        ordersQueue.registerObserver(observer);

        // when
        ordersQueue.add(onSiteOrder);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, ordersQueue.size());
    }

    @Test
    void shouldGetOrderFromQueue() {
        // given
        ordersQueue.add(onSiteOrder);
        ordersQueue.add(deliveryOrder);

        // when
        final SimpleOrder removedOrder = ordersQueue.get();

        // then
        assertEquals(onSiteOrder, removedOrder);
        assertEquals(1, ordersQueue.size());
    }

    @Test
    void shouldReturnQueueSize() {
        // given
        ordersQueue.add(onSiteOrder);
        ordersQueue.add(deliveryOrder);

        // when
        final int queueSize = ordersQueue.size();

        // then
        assertEquals(2, queueSize);
    }
}
