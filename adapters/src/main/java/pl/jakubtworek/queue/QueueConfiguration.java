package pl.jakubtworek.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtworek.SpringDomainEventPublisher;

@Configuration
class QueueConfiguration {
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

    @Bean
    Kitchen kitchen(
            SpringDomainEventPublisher publisher
    ) {
        return new Kitchen(
                publisher
        );
    }
}
