package pl.jakubtworek.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

interface SqlOrderRepository extends JpaRepository<OrderSnapshot, Long> {
    Optional<OrderSnapshot> findById(Long id);

    <S extends OrderSnapshot> S save(S entity);
}

interface SqlOrderQueryRepository extends OrderQueryRepository, JpaRepository<OrderSnapshot, Long> {
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
            "AND (:userId IS NULL OR o.user.id = :userId)")
    List<OrderDto> findFilteredOrders(
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("toDate") ZonedDateTime toDate,
            @Param("typeOfOrder") TypeOfOrder typeOfOrder,
            @Param("isReady") Boolean isReady,
            @Param("employeeId") Long employeeId,
            @Param("userId") Long userId);
}

@Repository
class OrderRepositoryImpl implements OrderRepository {

    private final SqlOrderRepository repository;

    OrderRepositoryImpl(final SqlOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return repository.findById(id).map(Order::restore);
    }

    @Override
    public Order save(final Order entity) {
        return Order.restore(repository.save(entity.getSnapshot()));
    }
}
