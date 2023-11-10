package pl.jakubtworek.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WaiterDeliveryTest {
    @Mock
    private DomainEventPublisher publisher;

    private WaiterDelivery waiterDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        waiterDelivery = new WaiterDelivery(publisher);
    }

    @Test
    void shouldNotHandleOrder() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.ON_SITE, 1, OrderEvent.State.READY
        );

        // when
        waiterDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "WAITER");

        // when
        waiterDelivery.handle(employee);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldHandleEmployeeAndOrderEvent() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "DELIVERY");
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.ON_SITE, 1, OrderEvent.State.READY
        );

        // when
        waiterDelivery.handle(employee);
        waiterDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(1)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldTwoTimesHandleEmployeeAndOrderEvent() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "DELIVERY");
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.ON_SITE, 1, OrderEvent.State.READY
        );

        // when
        waiterDelivery.handle(employee);
        waiterDelivery.handle(orderEvent);
        waiterDelivery.handle(employee);
        waiterDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(2)).publish(any(OrderEvent.class));
    }
}
