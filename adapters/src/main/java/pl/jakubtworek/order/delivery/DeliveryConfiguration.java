package pl.jakubtworek.order.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.common.SpringDomainEventPublisher;

@Configuration
class DeliveryConfiguration {

    @Value("${multiplierTimeToCook}")
    private Long multiplierTimeToCook;

    @Value("${timeToDelivery}")
    private Long timeToDelivery;

    @Bean
    Kitchen kitchen(
            SpringDomainEventPublisher publisher
    ) {
        return new Kitchen(
                publisher,
                multiplierTimeToCook
        );
    }

    @Bean
    DeliveryHandler carDelivery(
            SpringDomainEventPublisher publisher
    ) {
        return new DeliveryHandler(
                publisher,
                timeToDelivery
        );
    }
}
