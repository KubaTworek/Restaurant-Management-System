package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OrdersQueueFacadeTest {

    @Mock
    private OrdersQueue ordersQueue;
    @Mock
    private OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    @Mock
    private OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    private OrdersQueueFacade ordersQueueFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ordersQueueFacade = new OrdersQueueFacade(ordersQueue, ordersMadeOnsiteQueue, ordersMadeDeliveryQueue);
    }

    @Test
    void testAddToQueue() {
        // given
        SimpleOrder order = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        // when
        ordersQueueFacade.addToQueue(order);

        // then
        verify(ordersQueue, times(1)).add(order);
    }

    @Test
    void testAddMadeOrderToQueue() {
        // given
        SimpleOrder onSiteOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        SimpleOrder deliveryOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        SimpleOrder takeawayOrder = new SimpleOrder(3L, 220, ZonedDateTime.now(), null, TypeOfOrder.TAKE_AWAY);

        // when
        ordersQueueFacade.addMadeOrderToQueue(onSiteOrder);
        ordersQueueFacade.addMadeOrderToQueue(deliveryOrder);
        ordersQueueFacade.addMadeOrderToQueue(takeawayOrder);

        // then
        verify(ordersMadeOnsiteQueue, times(1)).add(onSiteOrder);
        verify(ordersMadeDeliveryQueue, times(1)).add(deliveryOrder);
        verify(ordersMadeOnsiteQueue, times(1)).add(takeawayOrder);
    }
}

