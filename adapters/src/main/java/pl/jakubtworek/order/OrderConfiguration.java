package pl.jakubtworek.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.queue.OrdersQueueFacade;
import pl.jakubtworek.employee.EmployeeFacade;

@Configuration
class OrderConfiguration {
    @Bean
    OrderFacade orderFacade(
            UserFacade userFacade,
            EmployeeFacade employeeFacade,
            OrderRepository orderRepository,
            OrderQueryRepository orderQueryRepository,
            OrdersQueueFacade ordersQueueFacade
    ) {
        return new OrderFacade(
                userFacade,
                employeeFacade,
                orderRepository,
                orderQueryRepository,
                ordersQueueFacade
        );
    }
}
