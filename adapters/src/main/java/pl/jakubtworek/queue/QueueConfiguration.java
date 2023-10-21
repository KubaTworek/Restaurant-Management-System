package pl.jakubtworek.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.order.OrderFacade;

@Configuration
class QueueConfiguration {
    @Bean
    CarDelivery carDelivery(
            OrderFacade orderFacade,
            DeliveryQueue deliveryQueue,
            OrdersMadeDeliveryQueue ordersMadeDeliveryQueue
            ) {
        return new CarDelivery(
                orderFacade,
                deliveryQueue,
                ordersMadeDeliveryQueue
        );
    }

    @Bean
    WaiterDelivery waiterDelivery(
            OrderFacade orderFacade,
            WaiterQueue waiterQueue,
            OrdersMadeOnsiteQueue ordersMadeOnsiteQueue
            ) {
        return new WaiterDelivery(
                orderFacade,
                waiterQueue,
                ordersMadeOnsiteQueue
        );
    }

    @Bean
    Kitchen kitchen(
            OrdersQueueFacade ordersQueueFacade,
            OrdersQueue ordersQueue,
            CooksQueue cooksQueue,
            OrderFacade orderFacade
    ) {
        return new Kitchen(
                ordersQueueFacade,
                ordersQueue,
                cooksQueue,
                orderFacade
        );
    }

    @Bean
    OrdersQueueFacade ordersQueueFacade(
            OrdersQueue ordersQueue,
            OrdersMadeOnsiteQueue ordersMadeOnsiteQueue,
            OrdersMadeDeliveryQueue ordersMadeDeliveryQueue
    ) {
        return new OrdersQueueFacade(
                ordersQueue,
                ordersMadeOnsiteQueue,
                ordersMadeDeliveryQueue
        );
    }

    @Bean
    EmployeeQueueFacade employeeQueueFacade(
            CooksQueue cooksQueue,
            WaiterQueue waiterQueue,
            DeliveryQueue deliveryQueue
    ) {
        return new EmployeeQueueFacade(
                cooksQueue,
                waiterQueue,
                deliveryQueue
        );
    }

    @Bean
    WaiterQueue waiterQueue() {
        return new WaiterQueue();
    }

    @Bean
    DeliveryQueue deliveryQueue() {
        return new DeliveryQueue();
    }

    @Bean
    CooksQueue cooksQueue() {
        return new CooksQueue();
    }

    @Bean
    OrdersQueue ordersQueue() {
        return new OrdersQueue();
    }

    @Bean
    OrdersMadeDeliveryQueue ordersMadeDeliveryQueue() {
        return new OrdersMadeDeliveryQueue();
    }

    @Bean
    OrdersMadeOnsiteQueue ordersMadeOnsiteQueue() {
        return new OrdersMadeOnsiteQueue();
    }
}
