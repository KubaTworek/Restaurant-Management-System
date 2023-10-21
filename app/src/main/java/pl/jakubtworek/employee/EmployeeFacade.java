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
    private static final String EMPLOYEE_NOT_FOUND_ERROR = "Employee with that id doesn't exist";
    private static final String INVALID_JOB_ERROR = "Invalid job type!!";

    EmployeeFacade(final EmployeeRepository employeeRepository, final EmployeeQueryRepository employeeQueryRepository, final EmployeeQueueFacade employeeQueueFacade) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.employeeQueueFacade = employeeQueueFacade;
    }

    public SimpleEmployee getById(Long id) {
        return employeeQueryRepository.findSimpleById(id)
                .orElseThrow(() -> new IllegalStateException(EMPLOYEE_NOT_FOUND_ERROR));
    }

    EmployeeDto save(EmployeeRequest toSave) {
        validateJob(toSave.getJob());

        final var employee = new Employee();
        employee.setFirstName(toSave.getFirstName());
        employee.setLastName(toSave.getLastName());
        employee.setJob(Job.valueOf(toSave.getJob()));

        final var created = toDto(employeeRepository.save(employee));
        final var employeeQueryDto = new SimpleEmployee(
                created.getId(),
                created.getFirstName(),
                created.getLastName(),
                created.getJob()
        );
        employeeQueueFacade.addToQueue(employeeQueryDto);

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

    public List<EmployeeDto> findByJob(String jobName) {
        validateJob(jobName);
        return employeeQueryRepository.findByJob(Job.valueOf(jobName));
    }

    private void validateJob(String jobName) {
        try {
            Job.valueOf(jobName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(INVALID_JOB_ERROR);
        }
    }

    private EmployeeDto toDto(Employee employee) {
        return EmployeeDto.create(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getJob());
    }
}