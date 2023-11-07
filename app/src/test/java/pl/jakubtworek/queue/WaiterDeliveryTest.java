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
    void shouldHandleEvents() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L,
                TypeOfOrder.ON_SITE,
                4,
                OrderEvent.State.READY
        );
        final var employeeEvent = new EmployeeEvent(
                1L,
                null,
                Job.WAITER
        );

        // when
        waiterDelivery.handle(orderEvent);
        waiterDelivery.handle(employeeEvent);

        // then
        Thread.sleep(10);
        verify(publisher, times(1)).publish(any(OrderEvent.class));
        verify(publisher, times(1)).publish(any(EmployeeEvent.class));
    }

    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var employeeEvent = new EmployeeEvent(
                1L,
                null,
                Job.WAITER
        );

        // when
        waiterDelivery.handle(employeeEvent);

        // then
        Thread.sleep(10);
        verify(publisher, times(0)).publish(any());
    }

    @Test
    void shouldNotHandleOrderEvent() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L,
                TypeOfOrder.ON_SITE,
                4,
                OrderEvent.State.READY
        );

        // when
        waiterDelivery.handle(orderEvent);

        // then
        Thread.sleep(10);
        verify(publisher, times(0)).publish(any());
    }
}