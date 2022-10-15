package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.Optional;

@Repository
public interface TypeOfOrderRepository extends JpaRepository<TypeOfOrder, Integer> {
    Optional<TypeOfOrder> findByType(String theType);
}
