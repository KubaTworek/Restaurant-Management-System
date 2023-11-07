package pl.jakubtworek.queue;

import pl.jakubtworek.DomainEventPublisher;

class WaiterDelivery extends AbstractDelivery{
    public WaiterDelivery(final DomainEventPublisher publisher) {
        super(publisher);
    }
}
