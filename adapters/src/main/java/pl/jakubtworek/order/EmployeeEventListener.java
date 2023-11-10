package pl.jakubtworek.order;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.vo.EmployeeEvent;

import static pl.jakubtworek.employee.dto.Job.COOK;

@Service
class EmployeeEventListener {
    private final Kitchen kitchen;

    EmployeeEventListener(final Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    @EventListener
    public void on(EmployeeEvent event) {
        if (COOK.equals(event.getJob())) {
            kitchen.handle(event);
        }
    }
}
