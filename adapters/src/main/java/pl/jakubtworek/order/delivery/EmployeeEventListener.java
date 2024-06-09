package pl.jakubtworek.order.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.vo.EmployeeEvent;

@Service
class EmployeeEventListener {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeEventListener.class);

    private final Kitchen kitchen;
    private final DeliveryHandler deliveryHandler;

    EmployeeEventListener(final Kitchen kitchen,
                          final DeliveryHandler deliveryHandler
    ) {
        this.kitchen = kitchen;
        this.deliveryHandler = deliveryHandler;
    }

    @EventListener
    public void on(EmployeeEvent event) {
        logger.info("Handling employee event: {}", event);

        switch (event.getJob()) {
            case DELIVERY -> {
                logger.info("Handling DELIVERY job for employee: {}", event.getEmployeeId());
                deliveryHandler.handle(event);
            }
            case COOK -> {
                logger.info("Handling COOK job for employee: {}", event.getEmployeeId());
                kitchen.handle(event);
            }
            default -> logger.warn("Unknown job type: {}", event.getJob());
        }
    }
}
