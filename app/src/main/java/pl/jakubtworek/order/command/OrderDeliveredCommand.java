package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record OrderDeliveredCommand(
        Long orderId
) implements Command {
}
