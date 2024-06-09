package pl.jakubtworek.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.common.SpringDomainEventPublisher;
import pl.jakubtworek.menu.MenuItemFacade;

@Configuration
class OrderConfiguration {
    @Value("${timeToWaitForReceive}")
    private Long timeToWaitForReceive;

    @Bean
    @Scope("prototype")
    Order order(
            SpringDomainEventPublisher publisher,
            OrderRepository repository
    ) {
        Order order = new Order();
        order.setDependencies(publisher, repository, timeToWaitForReceive);
        return order;
    }

    @Bean
    OrderFacade orderFacade(
            UserFacade userFacade,
            MenuItemFacade menuItemFacade,
            OrderQueryRepository orderQueryRepository,
            Order order
    ) {
        return new OrderFacade(
                userFacade,
                menuItemFacade,
                orderQueryRepository,
                order
        );
    }

    @Bean
    OrderPreparedCommandHandler orderPreparedCommandHandler(
            Order order
    ) {
        return new OrderPreparedCommandHandler(
                order
        );
    }

    @Bean
    OrderStartDeliveryCommandHandler orderStartDeliveryCommandHandler(
            Order order
    ) {
        return new OrderStartDeliveryCommandHandler(
                order
        );
    }

    @Bean
    OrderDeliveredCommandHandler deliverOrderCommandHandler(
            Order order
    ) {
        return new OrderDeliveredCommandHandler(
                order
        );
    }

    @Bean
    AddEmployeeToOrderCommandHandler addEmployeeToOrderCommandHandler(
            Order order
    ) {
        return new AddEmployeeToOrderCommandHandler(
                order
        );
    }

    @Bean
    AddEmployeeToDeliveryCommandHandler addEmployeeToDeliveryCommandHandler(
            Order order
    ) {
        return new AddEmployeeToDeliveryCommandHandler(
                order
        );
    }
}
