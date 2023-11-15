package pl.jakubtworek.order.delivery;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class KitchenTest {
    @Test
    void shouldNotHandleOrderEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var kitchen = new Kitchen(publisher);
        final var orderEvent = new OrderEvent(1L, 2L, TypeOfOrder.TAKE_AWAY, 3, OrderEvent.State.TODO);

        // when
        kitchen.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var kitchen = new Kitchen(publisher);
        final var employeeEvent = new EmployeeEvent(1L, 2L, Job.COOK);

        // when
        kitchen.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldHandleEmployeeAndOrderEvent() throws InterruptedException {
        // given
        final var publisher = mock(DomainEventPublisher.class);
        final var kitchen = new Kitchen(publisher);
        final var employeeEvent = new EmployeeEvent(1L, 2L, Job.COOK);
        final var orderEvent = new OrderEvent(1L, 2L, TypeOfOrder.TAKE_AWAY, 3, OrderEvent.State.TODO);

        // when
        kitchen.handle(employeeEvent);
        kitchen.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(2)).publish(any());
    }
}
