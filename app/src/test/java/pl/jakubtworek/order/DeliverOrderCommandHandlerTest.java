package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.order.command.DeliverOrderCommand;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliverOrderCommandHandlerTest {
    @Mock
    private OrderFacade orderFacade;
    @Mock
    private OrderRepository orderRepository;

    private DeliverOrderCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new DeliverOrderCommandHandler(
                orderFacade,
                orderRepository
        );
    }

    @Test
    void shouldDeliverOrder() {
        // given
        final var command = new DeliverOrderCommand(1L);
        final var order = new Order();

        when(orderFacade.getById(1L)).thenReturn(order);

        // when
        handler.handle(command);

        // then
        assertNotNull(order.getSnapshot().getHourAway());
        verify(orderRepository, times(1)).save(order);
    }
}
