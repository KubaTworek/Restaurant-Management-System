package pl.jakubtworek.order.delivery;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class WaiterDeliveryTest {
    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var waiterDelivery = new WaiterDelivery(publisher);
        final var employeeEvent = new EmployeeEvent(1L, 2L, Job.WAITER);

        // when
        waiterDelivery.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldNotHandleOrderEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var waiterDelivery = new WaiterDelivery(publisher);
        final var orderEvent = new OrderEvent(1L, 2L, TypeOfOrder.ON_SITE, 3, OrderEvent.State.READY);

        // when
        waiterDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldHandleOrderAndEmployeeEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var waiterDelivery = new WaiterDelivery(publisher);
        final var orderEvent = new OrderEvent(1L, 2L, TypeOfOrder.ON_SITE, 3, OrderEvent.State.READY);
        final var employeeEvent = new EmployeeEvent(1L, 2L, Job.WAITER);

        // when
        waiterDelivery.handle(orderEvent);
        waiterDelivery.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(2)).publish(any());
    }
}
