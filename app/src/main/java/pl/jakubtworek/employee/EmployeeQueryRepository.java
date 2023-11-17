package pl.jakubtworek.employee;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.Job;

import java.util.Optional;
import java.util.Set;

interface EmployeeQueryRepository {
    Set<EmployeeDto> findByJob(Job job);

    Optional<EmployeeDto> findDtoById(Long id);

    Set<EmployeeDto> findDtoByStatus(Status status);
}
