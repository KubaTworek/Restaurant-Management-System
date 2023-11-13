package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public class AddEmployeeToOrderCommand implements Command {
    private final Long orderId;
    private final Long employeeId;

    public AddEmployeeToOrderCommand(Long orderId, Long employeeId) {
        this.orderId = orderId;
        this.employeeId = employeeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
