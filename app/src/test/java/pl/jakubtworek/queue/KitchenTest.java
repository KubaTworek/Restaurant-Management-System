package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KitchenTest {
    @Mock
    private OrdersQueueFacade ordersQueueFacade;
    @Mock
    private OrdersQueue ordersQueue;
    @Mock
    private CooksQueue cooksQueue;
    @Mock
    private OrderFacade orderFacade;

    private Kitchen kitchen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kitchen = new Kitchen(ordersQueueFacade, ordersQueue, cooksQueue, orderFacade);
    }

    @Test
    void shouldStartCookingWhenCookAndOrderExist() {
        // given
        when(ordersQueue.size()).thenReturn(1);
        when(cooksQueue.size()).thenReturn(1);

        // when
        kitchen.update();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(any(), any());
        verify(orderFacade, times(1)).getNumberOfMenuItems(any());
    }

    @Test
    void shouldNotStartCookingWhenCookOrOrderIsMissing() {
        // given
        when(ordersQueue.size()).thenReturn(0);
        when(cooksQueue.size()).thenReturn(1);

        // when
        kitchen.update();

        // then
        verify(orderFacade, times(0)).addEmployeeToOrder(any(), any());
        verify(orderFacade, times(0)).getNumberOfMenuItems(any());
    }

    @Test
    void shouldStartCooking() {
        // given
        final var cook = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var order = new SimpleOrder(1L, 120, ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), TypeOfOrder.DELIVERY);

        when(ordersQueue.size()).thenReturn(1);
        when(cooksQueue.size()).thenReturn(1);
        when(ordersQueue.get()).thenReturn(order);
        when(cooksQueue.get()).thenReturn(cook);
        when(orderFacade.getNumberOfMenuItems(order)).thenReturn(3);

        // when
        kitchen.update();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(order, cook);
        verify(orderFacade, times(1)).getNumberOfMenuItems(order);
    }
}
