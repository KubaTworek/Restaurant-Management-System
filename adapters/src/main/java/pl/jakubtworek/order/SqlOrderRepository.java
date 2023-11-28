package pl.jakubtworek.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

interface SqlOrderRepository extends JpaRepository<OrderSnapshot, Long> {
    @Query("SELECT o FROM OrderSnapshot o " +
            "LEFT JOIN FETCH o.orderItems " +
            "LEFT JOIN FETCH o.employees " +
            "WHERE o.id = :orderId")
    Optional<OrderSnapshot> findById(@Param("orderId") Long id);
}

interface SqlOrderQueryRepository extends OrderQueryRepository, JpaRepository<OrderSnapshot, Long> {
    @Query("SELECT o FROM OrderSnapshot o " +
            "LEFT JOIN FETCH o.orderItems " +
            "WHERE o.id = :orderId")
    Optional<OrderDto> findDtoById(@Param("orderId") Long id);

    @Query("SELECT o FROM OrderSnapshot o " +
            "LEFT JOIN FETCH o.orderItems " +
            "WHERE o.customerId = :customerId")
    List<OrderDto> findDtoByCustomerId(@Param("customerId") CustomerId customerId);

    @Query("SELECT o FROM OrderSnapshot o " +
            "LEFT JOIN FETCH o.orderItems " +
            "LEFT JOIN FETCH o.employees e " +
            "WHERE " +
            "(:fromDate IS NULL OR o.hourOrder >= :fromDate) " +
            "AND (:toDate IS NULL OR o.hourOrder <= :toDate) " +
            "AND (:typeOfOrder IS NULL OR o.typeOfOrder = :typeOfOrder) " +
            "AND (:isReady IS NULL OR " +
            "    (:isReady = true AND o.hourAway IS NOT NULL) OR " +
            "    (:isReady = false AND o.hourAway IS NULL)) " +
            "AND (:employeeId IS NULL OR e.id = :employeeId) " +
            "AND (:customerId IS NULL OR o.customerId.id = :customerId)")
    List<OrderDto> findFilteredOrders(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("typeOfOrder") TypeOfOrder typeOfOrder,
            @Param("isReady") Boolean isReady,
            @Param("employeeId") Long employeeId,
            @Param("customerId") Long customerId);
}

@Repository
class OrderRepositoryImpl implements OrderRepository {

    private final SqlOrderRepository repository;

    OrderRepositoryImpl(final SqlOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return repository.findById(id).map(o -> Order.restore(o, 1));
    }

    @Override
    public Order save(final Order entity) {
        return Order.restore(repository.save(entity.getSnapshot(1)), 1);
    }
}
