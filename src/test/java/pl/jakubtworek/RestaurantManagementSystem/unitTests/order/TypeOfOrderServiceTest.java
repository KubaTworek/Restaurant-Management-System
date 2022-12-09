package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.createOnsiteType;

public class TypeOfOrderServiceTest {
    @Mock
    private TypeOfOrderRepository typeOfOrderRepository;

    private TypeOfOrderService typeOfOrderService;

    @BeforeEach
    public void setup(){
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);

        typeOfOrderService = new TypeOfOrderServiceImpl(
                typeOfOrderRepository
        );
    }

    @Test
    public void shouldReturnTypeOfOrder_whenCorrectNameIsPassed() {
        // given
        Optional<TypeOfOrder> typeOfOrder = Optional.of(createOnsiteType());

        // when
        when(typeOfOrderRepository.findByType("Type")).thenReturn(typeOfOrder);

        Optional<TypeOfOrderDTO> typeOfOrderReturned = typeOfOrderService.findByType("Type");

        // then
        assertNotNull(typeOfOrderReturned);
    }
}
