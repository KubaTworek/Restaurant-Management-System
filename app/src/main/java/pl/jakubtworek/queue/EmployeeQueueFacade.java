package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;

public class EmployeeQueueFacade {
    private final CooksQueue cooksQueue;
    private final WaiterQueue waiterQueue;
    private final DeliveryQueue deliveryQueue;

    EmployeeQueueFacade(final CooksQueue cooksQueue, final WaiterQueue waiterQueue, final DeliveryQueue deliveryQueue) {
        this.cooksQueue = cooksQueue;
        this.waiterQueue = waiterQueue;
        this.deliveryQueue = deliveryQueue;
    }

    public void addToQueue(SimpleEmployee employee) {
        final var job = employee.getJob();

        switch (job) {
            case COOK:
                cooksQueue.add(employee);
                break;
            case WAITER:
                waiterQueue.add(employee);
                break;
            case DELIVERY:
                deliveryQueue.add(employee);
                break;
            default:
                throw new IllegalArgumentException("Unsupported job: " + job);
        }
    }
}
