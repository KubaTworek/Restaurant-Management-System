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

class CarDeliveryTest {
    @Mock
    private DomainEventPublisher publisher;

    private CarDelivery carDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        carDelivery = new CarDelivery(publisher);
    }

    @Test
    void shouldNotHandleOrderEvent() throws InterruptedException {
        // given
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.DELIVERY, 1, OrderEvent.State.READY
        );

        // when
        carDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldNotHandleEmployee() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "DELIVERY");

        // when
        carDelivery.handle(employee);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldHandleEmployeeEventAndOrder() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "DELIVERY");
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.DELIVERY, 1, OrderEvent.State.READY
        );

        // when
        carDelivery.handle(employee);
        carDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(1)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldTwoTimesHandleEmployeeEventAndOrder() throws InterruptedException {
        // given
        final var employee = new Employee();
        employee.updateInfo("Jon", "Doe", "DELIVERY");
        final var orderEvent = new OrderEvent(
                1L, 1L, TypeOfOrder.DELIVERY, 1, OrderEvent.State.READY
        );

        // when
        carDelivery.handle(employee);
        carDelivery.handle(orderEvent);
        carDelivery.handle(employee);
        carDelivery.handle(orderEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(2)).publish(any(OrderEvent.class));
    }
}
