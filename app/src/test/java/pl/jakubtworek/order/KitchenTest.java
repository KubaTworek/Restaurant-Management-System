package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.vo.OrderEvent;

import java.util.Set;

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
    void shouldNotHandleOrder() throws InterruptedException {
        // given
        final var order = new Order();
        order.updateInfo(null, 0, "ON_SITE", null);

        // when
        kitchen.handle(order);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldNotHandleEmployeeEvent() throws InterruptedException {
        // given
        final var employeeEvent = new EmployeeEvent(1L, null, Job.COOK);

        // when
        kitchen.handle(employeeEvent);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldNotHandleEmployeeId() throws InterruptedException {
        // given
        final var employeeId = new EmployeeId(1L);

        // when
        kitchen.handle(employeeId);

        // then
        Thread.sleep(20);
        verify(publisher, times(0)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldHandleEmployeeEventAndOrder() throws InterruptedException {
        // given
        final var order = new Order();
        order.updateInfo(Set.of(new MenuItemId(1L)), 1000, "ON_SITE", null);
        final var employeeEvent = new EmployeeEvent(1L, null, Job.COOK);

        // when
        kitchen.handle(employeeEvent);
        kitchen.handle(order);

        // then
        Thread.sleep(20);
        verify(publisher, times(1)).publish(any(OrderEvent.class));
    }

    @Test
    void shouldTwoTimesHandleEmployeeEventAndOrder() throws InterruptedException {
        // given
        final var order = new Order();
        order.updateInfo(Set.of(new MenuItemId(1L)), 1000, "ON_SITE", null);
        final var employeeEvent = new EmployeeEvent(1L, null, Job.COOK);
        final var employeeId = new EmployeeId(1L);


        // when
        kitchen.handle(employeeEvent);
        kitchen.handle(order);
        kitchen.handle(employeeId);
        kitchen.handle(order);

        // then
        Thread.sleep(20);
        verify(publisher, times(2)).publish(any(OrderEvent.class));
    }
}
