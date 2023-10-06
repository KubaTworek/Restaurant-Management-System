package pl.jakubtworek.restaurant.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

@Repository
interface SimpleOrderQueryRepository extends JpaRepository<SimpleOrderQueryDto, Long> {
}
