package pl.jakubtworek.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.queue.OrdersQueueFacade;

@Configuration
class OrderConfiguration {
    @Bean
    OrderFacade orderFacade(
            UserFacade userFacade,
            EmployeeFacade employeeFacade,
            MenuItemFacade menuItemFacade,
            OrderRepository orderRepository,
            OrderQueryRepository orderQueryRepository,
            OrdersQueueFacade ordersQueueFacade
    ) {
        return new OrderFacade(
                userFacade,
                employeeFacade,
                menuItemFacade,
                orderRepository,
                orderQueryRepository,
                ordersQueueFacade
        );
    }
}
