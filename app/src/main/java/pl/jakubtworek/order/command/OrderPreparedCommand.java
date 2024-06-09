package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record OrderPreparedCommand (
        Long orderId
) implements Command {
}
