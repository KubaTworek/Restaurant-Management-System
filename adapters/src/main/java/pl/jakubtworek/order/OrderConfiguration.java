package pl.jakubtworek.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.common.SpringDomainEventPublisher;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.menu.MenuItemFacade;

@Configuration
class OrderConfiguration {
    @Bean
    OrderFacade orderFacade(
            UserFacade userFacade,
            EmployeeFacade employeeFacade,
            MenuItemFacade menuItemFacade,
            OrderFactory orderFactory,
            OrderRepository orderRepository,
            OrderQueryRepository orderQueryRepository,
            SpringDomainEventPublisher publisher
    ) {
        return new OrderFacade(
                userFacade,
                employeeFacade,
                menuItemFacade,
                orderFactory,
                orderRepository,
                orderQueryRepository,
                publisher);
    }

    @Bean
    DeliverOrderCommandHandler deliverOrderCommandHandler(
            OrderFacade orderFacade,
            OrderRepository orderRepository
    ) {
        return new DeliverOrderCommandHandler(
                orderFacade,
                orderRepository
        );
    }

    @Bean
    AddEmployeeToOrderCommandHandler addEmployeeToOrderCommandHandler(
            OrderFacade orderFacade,
            EmployeeFacade employeeFacade,
            OrderRepository orderRepository
    ) {
        return new AddEmployeeToOrderCommandHandler(
                orderFacade,
                employeeFacade,
                orderRepository
        );
    }

    @Bean
    OrderFactory orderFactory(
            UserFacade userFacade,
            MenuItemFacade menuItemFacade
    ) {
        return new OrderFactory(
                userFacade,
                menuItemFacade
        );
    }
}
