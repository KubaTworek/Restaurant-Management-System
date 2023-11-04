package pl.jakubtworek.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlOrderRepository extends JpaRepository<OrderSnapshot, Long> {
    Optional<OrderSnapshot> findById(Long id);

    <S extends OrderSnapshot> S save(S entity);
}

interface SqlOrderQueryRepository extends OrderQueryRepository, JpaRepository<OrderSnapshot, Long> {
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
