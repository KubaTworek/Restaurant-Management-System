package pl.jakubtworek.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlOrderRepository extends JpaRepository<SqlOrder, Long> {
    Optional<SqlOrder> findById(Long id);

    <S extends SqlOrder> S save(S entity);
}

interface SqlOrderQueryRepository extends OrderQueryRepository, JpaRepository<SqlOrder, Long> {
}

@Repository
class OrderRepositoryImpl implements OrderRepository {

    private final SqlOrderRepository repository;

    OrderRepositoryImpl(final SqlOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return repository.findById(id).map(SqlOrder::toOrder);
    }

    @Override
    public Order save(final Order entity) {
        return repository.save(SqlOrder.fromOrder(entity)).toOrder();
    }
}
