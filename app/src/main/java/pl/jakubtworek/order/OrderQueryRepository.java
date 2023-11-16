package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    Optional<OrderDto> findDtoById(Long id);

    List<OrderDto> findDtoByClientId(UserId userId);

    List<OrderDto> findFilteredOrders(
            ZonedDateTime fromDate,
            ZonedDateTime toDate,
            TypeOfOrder typeOfOrder,
            Boolean isReady,
            Long employeeId,
            Long userId
    );
}
