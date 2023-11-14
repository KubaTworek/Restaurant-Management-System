package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrderQueryRepository {
    List<OrderDto> findByUserId(Long userId);

    List<OrderDto> findFilteredOrders(
            ZonedDateTime fromDate,
            ZonedDateTime toDate,
            TypeOfOrder typeOfOrder,
            Boolean isReady,
            Long employeeId,
            Long userId
    );
}
