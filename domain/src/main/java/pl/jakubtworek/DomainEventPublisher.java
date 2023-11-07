package pl.jakubtworek;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
