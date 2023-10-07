package pl.jakubtworek.business;

import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

abstract class Delivery {
    final OrderFacade orderFacade;
    final EmployeeFacade employeeFacade;

    Delivery(final OrderFacade orderFacade, final EmployeeFacade employeeFacade) {
        this.orderFacade = orderFacade;
        this.employeeFacade = employeeFacade;
    }

    abstract void startDelivering();

    abstract boolean isExistsEmployeeAndOrder();

    abstract void delivering(SimpleEmployee employee, SimpleOrder order, int time);

    void startDeliveringOrder(SimpleEmployee employee, SimpleOrder order, int time) {
        Runnable r = () -> delivering(employee, order, time);
        new Thread(r).start();
    }
}
