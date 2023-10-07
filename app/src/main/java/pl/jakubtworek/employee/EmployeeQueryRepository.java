package pl.jakubtworek.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.SimpleEmployeeQueryDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeQueryRepository extends JpaRepository<Employee, Long> {
    List<EmployeeDto> findByJobName(String jobName);

    Optional<EmployeeDto> findDtoById(Long id);

    Optional<SimpleEmployeeQueryDto> findQueryById(Long id);

    List<EmployeeDto> findAllDtoBy();
}