package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.order.command.AddEmployeeToOrderCommand;
import pl.jakubtworek.common.vo.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddEmployeeToOrderCommandHandlerTest {
    @Mock
    private OrderFacade orderFacade;
    @Mock
    private EmployeeFacade employeeFacade;
    @Mock
    private OrderRepository orderRepository;

    private AddEmployeeToOrderCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new AddEmployeeToOrderCommandHandler(
                orderFacade,
                employeeFacade,
                orderRepository
        );
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // given
        final var command = new AddEmployeeToOrderCommand(1L, 1L);
        final var order = new Order();
        final var employee = EmployeeDto.create(1L, "Joe", "Doe", Job.COOK, Status.ACTIVE);

        when(orderFacade.getById(1L)).thenReturn(order);
        when(employeeFacade.getById(1L)).thenReturn(employee);

        // when
        handler.handle(command);

        // then
        assertEquals(1, order.getSnapshot(1).getEmployees().size());
        verify(orderRepository).save(order);
    }
}
