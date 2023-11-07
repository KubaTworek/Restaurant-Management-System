package pl.jakubtworek;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccurredOn();
}
