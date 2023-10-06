package pl.jakubtworek.restaurant.employee;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.business.queues.EmployeeQueueFacade;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueueFacade employeeQueueFacade;

    EmployeeService(final EmployeeRepository employeeRepository, final EmployeeQueueFacade employeeQueueFacade) {
        this.employeeRepository = employeeRepository;
        this.employeeQueueFacade = employeeQueueFacade;
    }

    EmployeeDto save(EmployeeRequest toSave) {

        Employee employee = new Employee();
        employee.setFirstName(toSave.getFirstName());
        employee.setLastName(toSave.getLastName());
        if (isJobExist(toSave.getJob())) {
            employee.setJob(Job.valueOf(toSave.getJob()));
        } else {
            throw new IllegalStateException("Job is not exist");
        }
        EmployeeDto created = new EmployeeDto(employeeRepository.save(employee));
        employeeQueueFacade.addEmployeeToProperQueue(created);

        return created;
    }

    void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
    }

    Optional<EmployeeDto> findById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeDto::new);
    }

    List<EmployeeDto> findByJob(String jobName) {
        return employeeRepository.findByJobName(jobName)
                .stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
    }

    private boolean isJobExist(String jobName) {
        for (Job job : Job.values()) {
            if (job.name().equals(jobName)) {
                return true;
            }
        }
        return false;
    }
}