package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.command.AddEmployeeToOrderCommand;

class AddEmployeeToOrderCommandHandler implements CommandHandler<AddEmployeeToOrderCommand> {
    private final OrderFacade orderFacade;
    private final EmployeeFacade employeeFacade;
    private final OrderRepository orderRepository;

    AddEmployeeToOrderCommandHandler(final OrderFacade orderFacade,
                                     final EmployeeFacade employeeFacade,
                                     final OrderRepository orderRepository
    ) {

        this.orderFacade = orderFacade;
        this.employeeFacade = employeeFacade;
        this.orderRepository = orderRepository;
    }

    @Override
    public void handle(AddEmployeeToOrderCommand command) {
        final var order = orderFacade.getById(command.getOrderId());
        final var employee = employeeFacade.getById(command.getEmployeeId());
        order.addEmployee(
                new EmployeeId(employee.getId())
        );
        orderRepository.save(order);
    }
}
