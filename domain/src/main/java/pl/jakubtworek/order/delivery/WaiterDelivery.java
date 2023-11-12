package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;

class WaiterDelivery extends AbstractDelivery {
    public WaiterDelivery(final DomainEventPublisher publisher) {
        super(publisher);
    }
}
