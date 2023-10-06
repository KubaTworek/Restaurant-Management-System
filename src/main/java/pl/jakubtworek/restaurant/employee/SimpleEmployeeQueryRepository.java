package pl.jakubtworek.restaurant.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;

@Repository
interface SimpleEmployeeQueryRepository extends JpaRepository<SimpleEmployeeQueryDto, Long> {
}