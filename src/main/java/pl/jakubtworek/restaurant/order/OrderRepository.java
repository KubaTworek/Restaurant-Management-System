package pl.jakubtworek.restaurant.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsername(String username);
}