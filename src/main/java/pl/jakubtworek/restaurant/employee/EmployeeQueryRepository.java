package pl.jakubtworek.restaurant.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;

import java.util.List;
import java.util.Optional;

@Repository
interface EmployeeQueryRepository extends JpaRepository<Employee, Long> {
    List<EmployeeDto> findByJobName(String jobName);

    Optional<EmployeeDto> findDtoById(Long id);

    Optional<SimpleEmployeeQueryDto> findQueryById(Long id);

    List<EmployeeDto> findAllDtoBy();
}