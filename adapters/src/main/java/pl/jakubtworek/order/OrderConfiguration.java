package pl.jakubtworek.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.SpringDomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.menu.MenuItemFacade;

@Configuration
class OrderConfiguration {
    @Bean
    OrderFacade orderFacade(
            UserFacade userFacade,
            EmployeeFacade employeeFacade,
            MenuItemFacade menuItemFacade,
            OrderRepository orderRepository,
            OrderQueryRepository orderQueryRepository,
            Kitchen kitchen
    ) {
        return new OrderFacade(
                userFacade,
                employeeFacade,
                menuItemFacade,
                orderRepository,
                orderQueryRepository,
                kitchen
        );
    }

    @Bean
    Kitchen kitchen(
            SpringDomainEventPublisher publisher
    ) {
        return new Kitchen(
                publisher
        );
    }
}
