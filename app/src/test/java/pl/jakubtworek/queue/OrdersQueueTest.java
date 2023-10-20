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
    void testAdd_AddOrderToQueue() {
        // given
        Observer observer = mock(Observer.class);
        ordersQueue.registerObserver(observer);

        // when
        ordersQueue.add(onSiteOrder);

        // then
        verify(observer, times(1)).update();
        assertEquals(1, ordersQueue.size());
    }

    @Test
    void testGet_GetOrderFromQueue() {
        // given
        ordersQueue.add(onSiteOrder);
        ordersQueue.add(deliveryOrder);

        // when
        SimpleOrder removedOrder = ordersQueue.get();

        // then
        assertEquals(onSiteOrder, removedOrder);
        assertEquals(1, ordersQueue.size());
    }

    @Test
    void testSize_ReturnQueueSize() {
        // given
        ordersQueue.add(onSiteOrder);
        ordersQueue.add(deliveryOrder);

        // when
        int queueSize = ordersQueue.size();

        // then
        assertEquals(2, queueSize);
    }

    @Test
    void testOrderComparator_OnSiteBeforeDelivery() {
        // given
        OrdersQueue.OrderComparator comparator = new OrdersQueue.OrderComparator();

        // when
        int comparisonResult = comparator.compare(onSiteOrder, deliveryOrder);

        // then
        assertEquals(-1, comparisonResult);
    }

    @Test
    void testOrderComparator_DeliveryBeforeOnSite() {
        // given
        OrdersQueue.OrderComparator comparator = new OrdersQueue.OrderComparator();

        // when
        int comparisonResult = comparator.compare(deliveryOrder, onSiteOrder);

        // then
        assertEquals(1, comparisonResult);
    }

    @Test
    void testOrderComparator_TakeAwayBeforeOnSite() {
        // given
        OrdersQueue.OrderComparator comparator = new OrdersQueue.OrderComparator();

        // when
        int comparisonResult = comparator.compare(takeAwayOrder, onSiteOrder);

        // then
        assertEquals(0, comparisonResult);
    }
}

