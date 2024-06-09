package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.command.AddEmployeeToDeliveryCommand;

class AddEmployeeToDeliveryCommandHandler implements CommandHandler<AddEmployeeToDeliveryCommand> {
    private final Order order;

    AddEmployeeToDeliveryCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(AddEmployeeToDeliveryCommand command) {
        order.addEmployeeToDelivery(
                command.orderId(),
                new EmployeeId(command.employeeId())
        );
    }
}