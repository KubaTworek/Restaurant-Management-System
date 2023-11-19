package pl.jakubtworek.employee;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.vo.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeFacade {
    private final EmployeeQueryRepository employeeQueryRepository;
    private final Employee employee;


    EmployeeFacade(final EmployeeQueryRepository employeeQueryRepository,
                   final Employee employee
    ) {
        this.employeeQueryRepository = employeeQueryRepository;
        this.employee = employee;
    }

    EmployeeDto save(EmployeeRequest toSave) {
        final var created = employee.from(
                toSave.firstName(),
                toSave.lastName(),
                toSave.job()
        );
        return toDto(created);
    }

    void deleteById(Long id) {
        employee.deactivate(id);
    }

    List<EmployeeDto> findAll() {
        return new ArrayList<>(employeeQueryRepository.findDtoByStatus(Status.ACTIVE));
    }

    Optional<EmployeeDto> findById(Long id) {
        return employeeQueryRepository.findDtoById(id);
    }

    List<EmployeeDto> findByJob(String jobName) {
        return new ArrayList<>(employeeQueryRepository.findByJob(Job.valueOf(jobName)));
    }

    private EmployeeDto toDto(Employee employee) {
        final var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob(), snap.getStatus());
    }
}
