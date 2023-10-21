package pl.jakubtworek.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class QueueTestIT {

    @Mock
    private OrderFacade orderFacade;
    @Captor
    private ArgumentCaptor<SimpleEmployee> employeeCaptor;
    @Captor
    private ArgumentCaptor<SimpleOrder> orderCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCookOnsiteOrderBeforeDelivery() throws InterruptedException {
        // given
        final var testSetup = new TestSetup(orderFacade);
        final var employeeQueueFacade = testSetup.getEmployeeQueueFacade();
        final var ordersQueueFacade = testSetup.getOrdersQueueFacade();
        final var inOrder = inOrder(orderFacade);

        final var cook = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var waiter = new SimpleEmployee(2L, "Bob", "Burton", Job.WAITER);
        final var delivery = new SimpleEmployee(3L, "Alice", "Patel", Job.DELIVERY);
        final var deliveryOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        final var onsiteOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        // when
        ordersQueueFacade.addToQueue(deliveryOrder);
        ordersQueueFacade.addToQueue(onsiteOrder);
        addEmployeesToProperQueues(employeeQueueFacade, cook, waiter, delivery);

        // then
        Thread.sleep(5);
        inOrder.verify(orderFacade, times(1)).setAsDelivered(onsiteOrder);
        inOrder.verify(orderFacade, times(1)).setAsDelivered(deliveryOrder);
    }

    @Test
    void everyOrderShouldBeMadeBySpecificEmployee() {
        // given
        final var testSetup = new TestSetup(orderFacade);
        final var employeeQueueFacade = testSetup.getEmployeeQueueFacade();
        final var ordersQueueFacade = testSetup.getOrdersQueueFacade();
        final var inOrder = inOrder(orderFacade);

        final var cook = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var waiter = new SimpleEmployee(2L, "Bob", "Burton", Job.WAITER);
        final var delivery = new SimpleEmployee(3L, "Alice", "Patel", Job.DELIVERY);
        final var deliveryOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        final var onsiteOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        // when
        addEmployeesToProperQueues(employeeQueueFacade, cook, waiter, delivery);

        // then
        ordersQueueFacade.addToQueue(onsiteOrder);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(onsiteOrder, cook);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(onsiteOrder, waiter);
        inOrder.verify(orderFacade, times(1)).setAsDelivered(onsiteOrder);

        ordersQueueFacade.addToQueue(deliveryOrder);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(deliveryOrder, cook);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(deliveryOrder, delivery);
        inOrder.verify(orderFacade, times(1)).setAsDelivered(deliveryOrder);
    }

    @Test
    void shouldCookTheOtherCook_whenThereIsMoreThanOneOrderInQueue() {
        // given
        final var testSetup = new TestSetup(orderFacade);
        final var employeeQueueFacade = testSetup.getEmployeeQueueFacade();
        final var ordersQueueFacade = testSetup.getOrdersQueueFacade();
        final var inOrder = inOrder(orderFacade);

        final var cook1 = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var cook2 = new SimpleEmployee(2L, "James", "Moore", Job.COOK);
        final var waiter = new SimpleEmployee(3L, "Bob", "Burton", Job.WAITER);
        final var delivery = new SimpleEmployee(4L, "Alice", "Patel", Job.DELIVERY);
        final var deliveryOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        final var onsiteOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        // when
        addEmployeesToProperQueues(employeeQueueFacade, cook1, cook2, waiter, delivery);

        // then
        ordersQueueFacade.addToQueue(onsiteOrder);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(onsiteOrder, cook1);
        ordersQueueFacade.addToQueue(deliveryOrder);
        inOrder.verify(orderFacade, times(1)).addEmployeeToOrder(deliveryOrder, cook2);
    }

    @Test
    void shouldCookTheSameCook_whenThereIsOnlyOneOrderInQueue() throws InterruptedException {
        // given
        final var testSetup = new TestSetup(orderFacade);
        final var employeeQueueFacade = testSetup.getEmployeeQueueFacade();
        final var ordersQueueFacade = testSetup.getOrdersQueueFacade();

        final var cook1 = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var cook2 = new SimpleEmployee(2L, "James", "Moore", Job.COOK);
        final var waiter = new SimpleEmployee(3L, "Bob", "Burton", Job.WAITER);
        final var delivery = new SimpleEmployee(4L, "Alice", "Patel", Job.DELIVERY);
        final var deliveryOrder = new SimpleOrder(1L, 120, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY);
        final var onsiteOrder = new SimpleOrder(2L, 300, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        // when
        addEmployeesToProperQueues(employeeQueueFacade, cook1, cook2, waiter, delivery);

        // then
        ordersQueueFacade.addToQueue(onsiteOrder);
        verify(orderFacade, times(2)).addEmployeeToOrder(orderCaptor.capture(), employeeCaptor.capture());
        final var cookCapture1 = employeeCaptor.getAllValues().get(0);

        Thread.sleep(5);

        ordersQueueFacade.addToQueue(deliveryOrder);
        verify(orderFacade, times(4)).addEmployeeToOrder(orderCaptor.capture(), employeeCaptor.capture());
        final var cookCapture2 = employeeCaptor.getAllValues().get(2);

        assertEquals(cookCapture1.getId(), cookCapture2.getId());
        assertEquals(cookCapture1.getJob(), cookCapture2.getJob());
    }

    private void addEmployeesToProperQueues(EmployeeQueueFacade employeeQueueFacade, SimpleEmployee... employees) {
        for (var employee : employees) {
            employeeQueueFacade.addToQueue(employee);
        }
    }

    private static class TestSetup {
        private final EmployeeQueueFacade employeeQueueFacade;
        private final OrdersQueueFacade ordersQueueFacade;

        public TestSetup(OrderFacade orderFacade) {
            var cooksQueue = new CooksQueue();
            var ordersQueue = new OrdersQueue();
            var deliveryQueue = new DeliveryQueue();
            var waiterQueue = new WaiterQueue();
            var ordersMadeDeliveryQueue = new OrdersMadeDeliveryQueue();
            var ordersMadeOnsiteQueue = new OrdersMadeOnsiteQueue();

            this.employeeQueueFacade = new EmployeeQueueFacade(
                    cooksQueue,
                    waiterQueue,
                    deliveryQueue
            );
            this.ordersQueueFacade = new OrdersQueueFacade(
                    ordersQueue,
                    ordersMadeOnsiteQueue,
                    ordersMadeDeliveryQueue
            );

            new CarDelivery(
                    orderFacade,
                    deliveryQueue,
                    ordersMadeDeliveryQueue
            );

            new WaiterDelivery(
                    orderFacade,
                    waiterQueue,
                    ordersMadeOnsiteQueue
            );

            new Kitchen(
                    ordersQueueFacade,
                    ordersQueue,
                    cooksQueue,
                    orderFacade
            );
        }

        public EmployeeQueueFacade getEmployeeQueueFacade() {
            return employeeQueueFacade;
        }

        public OrdersQueueFacade getOrdersQueueFacade() {
            return ordersQueueFacade;
        }
    }
}
