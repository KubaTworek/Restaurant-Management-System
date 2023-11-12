package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;

class CarDelivery extends AbstractDelivery {
    public CarDelivery(final DomainEventPublisher publisher) {
        super(publisher);
    }
}
