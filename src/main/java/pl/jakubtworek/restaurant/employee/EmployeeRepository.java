package pl.jakubtworek.restaurant.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EmployeeRepository extends JpaRepository<Employee, Long> {
    EmployeeDto saveAndReturnDto(Employee entity);
}