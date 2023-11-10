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
import static pl.jakubtworek.employee.dto.Job.DELIVERY;
import static pl.jakubtworek.employee.dto.Job.WAITER;

public class EmployeeFacade {
    private static final String EMPLOYEE_NOT_FOUND_ERROR = "Employee with that id doesn't exist";
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final WaiterDelivery waiterDelivery;
    private final CarDelivery carDelivery;
    private final DomainEventPublisher publisher;

    EmployeeFacade(final EmployeeRepository employeeRepository, final EmployeeQueryRepository employeeQueryRepository, final WaiterDelivery waiterDelivery, final CarDelivery carDelivery, final DomainEventPublisher publisher) {
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
        final var employee = new Employee();
        employee.updateInfo(
                toSave.getFirstName(),
                toSave.getLastName(),
                toSave.getJob()
        );

        final var created = employeeRepository.save(employee);

        handleDeliveryAndCookJobs(toSave, created);

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

    private void handleDeliveryAndCookJobs(EmployeeRequest toSave, Employee created) {
        if (WAITER.name().equals(toSave.getJob())) {
            waiterDelivery.handle(created);
        }
        if (DELIVERY.name().equals(toSave.getJob())) {
            carDelivery.handle(created);
        }
        if (COOK.name().equals(toSave.getJob())) {
            publisher.publish(new EmployeeEvent(created.getSnapshot().getId(), null, COOK));
        }
    }

    private EmployeeDto toDto(Employee employee) {
        var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob());
    }
}