package pl.jakubtworek.order.command;

import pl.jakubtworek.common.Command;

public record OrderStartDeliveryCommand (
        Long orderId
) implements Command {
}