package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.delivery.dto.EmployeeDelivery;
import pl.jakubtworek.order.delivery.dto.OrderDelivery;
import pl.jakubtworek.order.vo.OrderEvent;

class DeliveryHandler extends AbstractDelivery {
    public DeliveryHandler(final DomainEventPublisher publisher, final Long timeToDelivery) {
        super(publisher, timeToDelivery);
    }

    @Override
    void handle(final OrderEvent event) {
        queues.add(new OrderDelivery(event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems()));
        processDeliveries();
    }

    @Override
    void handle(final EmployeeEvent event) {
        queues.add(new EmployeeDelivery(event.getEmployeeId(), event.getJob()));
        processDeliveries();
    }
}
