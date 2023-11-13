package pl.jakubtworek.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.common.SpringDomainEventPublisher;

@Configuration
class DeliveryConfiguration {
    @Bean
    Kitchen kitchen(
            SpringDomainEventPublisher publisher
    ) {
        return new Kitchen(
                publisher
        );
    }

    @Bean
    CarDelivery carDelivery(
            SpringDomainEventPublisher publisher
    ) {
        return new CarDelivery(
                publisher
        );
    }

    @Bean
    WaiterDelivery waiterDelivery(
            SpringDomainEventPublisher publisher
    ) {
        return new WaiterDelivery(
                publisher
        );
    }
}
