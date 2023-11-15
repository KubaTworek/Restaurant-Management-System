package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record DeliverOrderCommand(
        Long orderId
) implements Command {
}
