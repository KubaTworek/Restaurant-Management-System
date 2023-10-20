package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.queue.EmployeeQueueFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeFacade {
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final EmployeeQueueFacade employeeQueueFacade;

    EmployeeFacade(final EmployeeRepository employeeRepository, final EmployeeQueryRepository employeeQueryRepository, final EmployeeQueueFacade employeeQueueFacade) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.employeeQueueFacade = employeeQueueFacade;
    }

    public SimpleEmployee getById(Long id) {
        return employeeQueryRepository.findSimpleById(id)
                .orElseThrow(() -> new IllegalStateException("Employee with that id doesn't exist"));
    }

    EmployeeDto save(EmployeeRequest toSave) {
        final var employee = new Employee();
        employee.setFirstName(toSave.getFirstName());
        employee.setLastName(toSave.getLastName());
        if (isJobExist(toSave.getJob())) {
            employee.setJob(Job.valueOf(toSave.getJob()));
        } else {
            throw new IllegalStateException("Job is not exist");
        }
        final var created = toDto(employeeRepository.save(employee));
        final var employeeQueryDto = new SimpleEmployee(
                created.getId(),
                created.getFirstName(),
                created.getLastName(),
                created.getJob()
        );
        employeeQueueFacade.addEmployeeToProperQueue(employeeQueryDto);

        return created;
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
        if (isJobExist(jobName)) {
            return employeeQueryRepository.findByJob(Job.valueOf(jobName));
        } else {
            throw new IllegalStateException("Job is not exist");
        }
    }

    private boolean isJobExist(String jobName) {
        for (var job : Job.values()) {
            if (job.name().equals(jobName)) {
                return true;
            }
        }
        return false;
    }

    private EmployeeDto toDto(Employee employee) {
        return EmployeeDto.create(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getJob());
    }
}