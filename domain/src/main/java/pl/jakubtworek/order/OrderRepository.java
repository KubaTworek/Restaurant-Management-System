package pl.jakubtworek.order;

import java.util.Optional;

interface OrderRepository {
    Optional<Order> findById(Long id);

    Order save(Order entity);
}