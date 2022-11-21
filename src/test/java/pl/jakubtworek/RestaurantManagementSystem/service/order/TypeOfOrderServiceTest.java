package pl.jakubtworek.RestaurantManagementSystem.service.order;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.createOnsiteType;

public class TypeOfOrderServiceTest {
    @Mock
    private TypeOfOrderRepository typeOfOrderRepository;

    @Autowired
    private TypeOfOrderService typeOfOrderService;


    @Test
    public void shouldReturnTypeOfOrder_whenCorrectNameIsPassed() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        when(typeOfOrderRepository.findByType("Type")).thenReturn(Optional.of(typeOfOrder));

        Optional<TypeOfOrderDTO> typeOfOrderReturned = typeOfOrderService.findByType("Type");

        // then
        assertNotNull(typeOfOrderReturned);
    }
}
