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

class CarDeliveryTest {
    @Mock
    private DeliveryQueue deliveryQueue;
    @Mock
    private OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;
    @Mock
    private OrderFacade orderFacade;

    private CarDelivery carDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        carDelivery = new CarDelivery(orderFacade, deliveryQueue, ordersMadeDeliveryQueue);
    }

    @Test
    void testUpdate_StartDeliveringWhenEmployeeAndOrderExist() {
        // given
        when(ordersMadeDeliveryQueue.size()).thenReturn(1);
        when(deliveryQueue.size()).thenReturn(1);

        // when
        carDelivery.update();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(any(), any());
        verify(deliveryQueue, times(1)).get();
        verify(ordersMadeDeliveryQueue, times(1)).get();
    }

    @Test
    void testUpdate_DoNotStartDeliveringWhenEmployeeOrOrderIsMissing() {
        // given
        when(ordersMadeDeliveryQueue.size()).thenReturn(0);
        when(deliveryQueue.size()).thenReturn(1);

        // when
        carDelivery.update();

        // then
        verify(orderFacade, times(0)).addEmployeeToOrder(any(), any());
        verify(deliveryQueue, times(0)).get();
        verify(ordersMadeDeliveryQueue, times(0)).get();
    }

    @Test
    void testStartDelivering() {
        // given
        SimpleEmployee employee = new SimpleEmployee(1L, "John", "Doe", Job.DELIVERY);
        SimpleOrder order = new SimpleOrder(1L, 120, ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), TypeOfOrder.DELIVERY);
        when(deliveryQueue.get()).thenReturn(employee);
        when(ordersMadeDeliveryQueue.get()).thenReturn(order);

        // when
        carDelivery.startDelivering();

        // then
        verify(orderFacade, times(1)).addEmployeeToOrder(order, employee);
        verify(deliveryQueue, times(1)).get();
        verify(ordersMadeDeliveryQueue, times(1)).get();
    }
}
