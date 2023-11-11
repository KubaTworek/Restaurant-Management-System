package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.vo.EmployeeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.jakubtworek.employee.dto.Job.COOK;

public class EmployeeFacade {
    private static final String EMPLOYEE_NOT_FOUND_ERROR = "Employee with that id doesn't exist";
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final WaiterDelivery waiterDelivery;
    private final CarDelivery carDelivery;
    private final DomainEventPublisher publisher;

    EmployeeFacade(final EmployeeRepository employeeRepository,
                   final EmployeeQueryRepository employeeQueryRepository,
                   final WaiterDelivery waiterDelivery,
                   final CarDelivery carDelivery,
                   final DomainEventPublisher publisher
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.waiterDelivery = waiterDelivery;
        this.carDelivery = carDelivery;
        this.publisher = publisher;
    }

    public EmployeeDto getById(Long id) {
        return employeeQueryRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalStateException(EMPLOYEE_NOT_FOUND_ERROR));
    }

    EmployeeDto save(EmployeeRequest toSave) {
        final var employee = EmployeeFactory.createEmployee(
                toSave.getFirstName(),
                toSave.getLastName(),
                toSave.getJob()
        );

        final var created = employeeRepository.save(employee);

        handleDeliveryAndCookJobs(created, Job.valueOf(toSave.getJob()));

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

    List<EmployeeDto> findByJob(String jobName) {
        return employeeQueryRepository.findByJob(Job.valueOf(jobName));
    }

    private void handleDeliveryAndCookJobs(Employee created, Job job) {
        switch (job) {
            case WAITER:
                waiterDelivery.handle(created);
                break;
            case DELIVERY:
                carDelivery.handle(created);
                break;
            case COOK:
                publisher.publish(new EmployeeEvent(created.getSnapshot().getId(), null, COOK));
                break;
        }
    }

    private EmployeeDto toDto(Employee employee) {
        final var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob());
    }
}