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

class WaiterDeliveryTest {
    @Mock
    private WaiterQueue waiterQueue;
    @Mock
    private OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    @Mock
    private OrderFacade orderFacade;

    private WaiterDelivery waiterDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        waiterDelivery = new WaiterDelivery(orderFacade, waiterQueue, ordersMadeOnsiteQueue);
    }

    @Test
    void shouldStartDeliveringWhenWaiterAndOrderExist() {
        // given
        when(ordersMadeOnsiteQueue.size()).thenReturn(1);
        when(waiterQueue.size()).thenReturn(1);

        // when
        waiterDelivery.update();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(any(), any());
        verify(waiterQueue, times(1)).get();
        verify(ordersMadeOnsiteQueue, times(1)).get();
    }

    @Test
    void shouldNotStartDeliveringWhenWaiterOrOrderIsMissing() {
        // given
        when(ordersMadeOnsiteQueue.size()).thenReturn(0);
        when(waiterQueue.size()).thenReturn(1);

        // when
        waiterDelivery.update();

        // then
        verify(orderFacade, times(0)).addEmployeeToOrder(any(), any());
        verify(waiterQueue, times(0)).get();
        verify(ordersMadeOnsiteQueue, times(0)).get();
    }

    @Test
    void shouldStartDelivering() {
        // given
        final var waiter = new SimpleEmployee(1L, "John", "Doe", Job.WAITER);
        final var order = new SimpleOrder(1L, 120, ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), TypeOfOrder.ON_SITE);

        when(ordersMadeOnsiteQueue.get()).thenReturn(order);
        when(waiterQueue.get()).thenReturn(waiter);

        // when
        waiterDelivery.startDelivering();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(order, waiter);
        verify(waiterQueue, times(1)).get();
        verify(ordersMadeOnsiteQueue, times(1)).get();
    }
}