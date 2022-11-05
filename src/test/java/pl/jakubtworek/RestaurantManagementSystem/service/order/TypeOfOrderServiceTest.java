package pl.jakubtworek.RestaurantManagementSystem.service.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.TypeOfOrderServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeOfOrderServiceTest {
    @Mock
    private TypeOfOrderRepository typeOfOrderRepository;

    private TypeOfOrderService typeOfOrderService;



    @BeforeEach
    public void setUp() {
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);

        typeOfOrderService = new TypeOfOrderServiceImpl(typeOfOrderRepository);
    }

    @Test
    public void shouldReturnTypeOfOrder_whenCorrectNameIsPassed() {
        // given
        Optional<TypeOfOrder> typeOfOrder = Optional.of(new TypeOfOrder());
        when(typeOfOrderRepository.findByType("Type")).thenReturn(typeOfOrder);

        // when
        Optional<TypeOfOrder> typeOfOrderReturned = typeOfOrderService.findByType("Type");

        // then
        assertNotNull(typeOfOrderReturned);
    }
}
