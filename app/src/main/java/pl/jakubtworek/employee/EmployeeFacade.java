package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeFacade {
    private static final String EMPLOYEE_NOT_FOUND_ERROR = "Employee with that id doesn't exist";
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final DomainEventPublisher publisher;

    EmployeeFacade(final EmployeeRepository employeeRepository, final EmployeeQueryRepository employeeQueryRepository, final DomainEventPublisher publisher) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.publisher = publisher;
    }

    public EmployeeDto getById(Long id) {
        return employeeQueryRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalStateException(EMPLOYEE_NOT_FOUND_ERROR));
    }

    EmployeeDto save(EmployeeRequest toSave) {
        final var employee = new Employee();
        employee.updateInfo(
                toSave.getFirstName(),
                toSave.getLastName(),
                toSave.getJob()
        );

        final var created = employeeRepository.save(employee);

        publisher.publish(created.sendToDelivery());

        return toDto(created);
    }

    void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    List<EmployeeDto> findAll() {
        return new ArrayList<>(employeeQueryRepository.findBy(EmployeeDto.class));
    }

    Optional<EmployeeDto> findById(Long id) {
        return employeeQueryRepository.findDtoById(id);
    }

    public List<EmployeeDto> findByJob(String jobName) {
        return employeeQueryRepository.findByJob(Job.valueOf(jobName));
    }

    private EmployeeDto toDto(Employee employee) {
        var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob());
    }
}