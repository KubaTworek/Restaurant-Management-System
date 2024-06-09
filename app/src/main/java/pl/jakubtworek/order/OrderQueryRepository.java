package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

interface OrderQueryRepository {
    Optional<OrderDto> findDtoById(Long id);

    List<OrderDto> findDtoByCustomerId(CustomerId customerId);

    List<OrderDto> findOngoingDtoByCustomerId(CustomerId customerId);

    List<OrderDto> findFilteredOrders(
            ZonedDateTime fromDate,
            ZonedDateTime toDate,
            TypeOfOrder typeOfOrder,
            Boolean isReady,
            Long employeeId,
            Long customerId
    );

}
