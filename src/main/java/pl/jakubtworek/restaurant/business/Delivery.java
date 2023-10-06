package pl.jakubtworek.restaurant.business;

import pl.jakubtworek.restaurant.employee.EmployeeFacade;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;
import pl.jakubtworek.restaurant.order.OrderFacade;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

abstract class Delivery {
    final OrderFacade orderFacade;
    final EmployeeFacade employeeFacade;

    Delivery(final OrderFacade orderFacade, final EmployeeFacade employeeFacade) {
        this.orderFacade = orderFacade;
        this.employeeFacade = employeeFacade;
    }

    abstract void startDelivering();

    abstract boolean isExistsEmployeeAndOrder();

    abstract void delivering(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time);

    void startDeliveringOrder(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time) {
        Runnable r = () -> delivering(employee, order, time);
        new Thread(r).start();
    }
}
