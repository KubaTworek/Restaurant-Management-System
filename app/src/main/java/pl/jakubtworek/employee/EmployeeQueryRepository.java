package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.Job;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeQueryRepository {
    List<EmployeeDto> findByJob(Job job);

    Optional<EmployeeDto> findDtoById(Long id);

    <T> Set<T> findBy(Class<T> type);
}