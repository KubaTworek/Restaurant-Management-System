package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;

class CarDelivery extends AbstractDelivery {
    public CarDelivery(final DomainEventPublisher publisher) {
        super(publisher);
    }
}
