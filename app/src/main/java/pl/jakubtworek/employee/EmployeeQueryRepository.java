package pl.jakubtworek.employee;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.Job;

import java.util.Optional;
import java.util.Set;

interface EmployeeQueryRepository {

    Optional<EmployeeDto> findDtoById(Long id);

    Set<EmployeeDto> findFilteredEmployees(Job job, Status active);
}
