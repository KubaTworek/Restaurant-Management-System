package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.command.AddEmployeeToOrderCommand;

class AddEmployeeToOrderCommandHandler implements CommandHandler<AddEmployeeToOrderCommand> {
    private final Order order;

    AddEmployeeToOrderCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(AddEmployeeToOrderCommand command) {
        order.addEmployee(
                command.orderId(),
                new EmployeeId(command.employeeId())
        );
    }
}
