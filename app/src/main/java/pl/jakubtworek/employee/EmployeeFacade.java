package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeFacade {
    private static final String EMPLOYEE_NOT_FOUND_ERROR = "Employee with that id doesn't exist";
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final DomainEventPublisher publisher;

    EmployeeFacade(final EmployeeRepository employeeRepository,
                   final EmployeeQueryRepository employeeQueryRepository,
                   final DomainEventPublisher publisher
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.publisher = publisher;
    }

    public EmployeeDto getById(Long id) {
        return employeeQueryRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalStateException(EMPLOYEE_NOT_FOUND_ERROR));
    }

    EmployeeDto save(EmployeeRequest toSave) {
        final var created = saveEmployee(toSave);
        publishEmployeeEvent(created.getSnapshot());
        return toDto(created);
    }

    int deleteById(Long id) {
        return employeeRepository.deactivateEmployee(id);
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

    private Employee saveEmployee(EmployeeRequest toSave) {
        final var created = EmployeeFactory.createEmployee(
                toSave.firstName(),
                toSave.lastName(),
                toSave.job()
        );
        return employeeRepository.save(created);
    }

    private void publishEmployeeEvent(EmployeeSnapshot employee) {
        publisher.publish(
                new EmployeeEvent(employee.getId(), null, employee.getJob())
        );
    }

    private EmployeeDto toDto(Employee employee) {
        final var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob(), snap.getStatus());
    }
}
