package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.order.delivery.AbstractDelivery;

class WaiterDelivery extends AbstractDelivery {
    public WaiterDelivery(final DomainEventPublisher publisher) {
        super(publisher);
    }
}
