package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class KitchenTest {
    @Mock
    private DomainEventPublisher publisher;

    private Kitchen kitchen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kitchen = new Kitchen(publisher);
    }

    @Test
    void shouldHandleEvents() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L,
                TypeOfOrder.ON_SITE,
                4,
                OrderEvent.State.TODO
        );
        final var employeeEvent = new EmployeeEvent(
                1L,
                null,
                Job.COOK
        );

        // when
        kitchen.handle(orderEvent);
        kitchen.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(1)).publish(any(OrderEvent.class));
        verify(publisher, times(1)).publish(any(EmployeeEvent.class));
    }

    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var employeeEvent = new EmployeeEvent(
                1L,
                null,
                Job.COOK
        );

        // when
        kitchen.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldNotHandleOrderEvent() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L,
                TypeOfOrder.ON_SITE,
                4,
                OrderEvent.State.TODO
        );

        // when
        kitchen.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any());
    }
}
