package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record AddEmployeeToOrderCommand(
        Long orderId,
        Long employeeId
) implements Command {}
