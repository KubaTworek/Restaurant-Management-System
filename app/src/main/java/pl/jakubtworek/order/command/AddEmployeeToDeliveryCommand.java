package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record AddEmployeeToDeliveryCommand(
        Long orderId,
        Long employeeId
) implements Command {
}