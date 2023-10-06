package pl.jakubtworek.restaurant.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface OrderQueryRepository extends JpaRepository<Order, Long> {
    Optional<SimpleOrderQueryDto> findSimpleById(Long id);

    Optional<OrderDto> findDtoById(Long id);

    List<OrderDto> findByUserUsername(String username);

    @Query("SELECT o FROM Order o WHERE " +
            "(:fromDate IS NULL OR o.hourOrder >= :fromDate) " +
            "AND (:toDate IS NULL OR o.hourOrder <= :toDate) " +
            "AND (:typeOfOrder IS NULL OR o.typeOfOrder = :typeOfOrder) " +
            "AND (:isReady IS NULL OR " +
            "    (:isReady = true AND o.hourAway IS NOT NULL) OR " +
            "    (:isReady = false AND o.hourAway IS NULL)) " +
            "AND (:employeeId IS NULL OR :employeeId MEMBER OF o.employees) " +
            "AND (:username IS NULL OR o.user.username = :username)")
    List<OrderDto> findFilteredOrders(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("typeOfOrder") String typeOfOrder,
            @Param("isReady") Boolean isReady,
            @Param("employeeId") Long employeeId,
            @Param("username") String username);
}
