package pl.jakubtworek.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderQueryRepository {
    Optional<OrderDto> findDtoById(Long id);

    List<OrderDto> findByUserUsername(String username);

    @Query("SELECT o FROM OrderSnapshot o " +
            "LEFT JOIN o.employees e " +
            "WHERE " +
            "(:fromDate IS NULL OR o.hourOrder >= :fromDate) " +
            "AND (:toDate IS NULL OR o.hourOrder <= :toDate) " +
            "AND (:typeOfOrder IS NULL OR o.typeOfOrder = :typeOfOrder) " +
            "AND (:isReady IS NULL OR " +
            "    (:isReady = true AND o.hourAway IS NOT NULL) OR " +
            "    (:isReady = false AND o.hourAway IS NULL)) " +
            "AND (:employeeId IS NULL OR e.id = :employeeId) " +
            "AND (:username IS NULL OR o.user.username = :username)")
    List<OrderDto> findFilteredOrders(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("typeOfOrder") TypeOfOrder typeOfOrder,
            @Param("isReady") Boolean isReady,
            @Param("employeeId") Long employeeId,
            @Param("username") String username);
}
