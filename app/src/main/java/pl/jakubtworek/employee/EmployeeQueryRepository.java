package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeQueryRepository {
    List<EmployeeDto> findByJobName(String jobName);

    Optional<EmployeeDto> findDtoById(Long id);

    Optional<SimpleEmployee> findQueryById(Long id);

    <T> Set<T> findBy(Class<T> type);
}