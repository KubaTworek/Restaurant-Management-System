package pl.jakubtworek.RestaurantManagementSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.RestaurantManagementSystem.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

public interface TypeOfOrderDAO extends JpaRepository<TypeOfOrder, Integer> {
    TypeOfOrder findByType(String theType);
}
