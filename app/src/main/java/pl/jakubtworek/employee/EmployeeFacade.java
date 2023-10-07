package pl.jakubtworek.employee;

import org.springframework.stereotype.Service;
import pl.jakubtworek.business.queues.EmployeeQueueFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployeeQueryDto;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrderQueryDto;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeFacade {
    private final OrderFacade orderFacade;
    private final EmployeeRepository employeeRepository;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final EmployeeQueueFacade employeeQueueFacade;

    EmployeeFacade(final OrderFacade orderFacade, final EmployeeRepository employeeRepository, final EmployeeQueryRepository employeeQueryRepository, final EmployeeQueueFacade employeeQueueFacade) {
        this.orderFacade = orderFacade;
        this.employeeRepository = employeeRepository;
        this.employeeQueryRepository = employeeQueryRepository;
        this.employeeQueueFacade = employeeQueueFacade;
    }

    public void addOrderToEmployee(final SimpleEmployeeQueryDto employee, final SimpleOrderQueryDto order) {
        Employee employeeEntity = employeeQueryRepository.findById(employee.getId())
                .orElseThrow(() -> new IllegalStateException("Employee with that id doesn't exist"));
        SimpleOrderQueryDto orderEntity = orderFacade.getById(order.getId());
        employeeEntity.add(orderEntity);
    }

    public SimpleEmployeeQueryDto getById(Long id) {
        return employeeQueryRepository.findQueryById(id)
                .orElseThrow(() -> new IllegalStateException("Employee with that id doesn't exist"));
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
        EmployeeDto created = toDto(employeeRepository.save(employee));
        SimpleEmployeeQueryDto employeeQueryDto = new SimpleEmployeeQueryDto();
        employeeQueryDto.setId(created.getId());
        employeeQueryDto.setFirstName(created.getFirstName());
        employeeQueryDto.setLastName(created.getLastName());
        employeeQueryDto.setJob(created.getJob());
        employeeQueueFacade.addEmployeeToProperQueue(employeeQueryDto);

        return created;
    }

    void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    List<EmployeeDto> findAll() {
        return employeeQueryRepository.findAllDtoBy();
    }

    Optional<EmployeeDto> findById(Long id) {
        return employeeQueryRepository.findDtoById(id);
    }

    List<EmployeeDto> findByJob(String jobName) {
        return employeeQueryRepository.findByJobName(jobName);
    }

    private boolean isJobExist(String jobName) {
        for (Job job : Job.values()) {
            if (job.name().equals(jobName)) {
                return true;
            }
        }
        return false;
    }

    private EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .withId(employee.getId())
                .withFirstName(employee.getFirstName())
                .withLastName(employee.getLastName())
                .withJob(employee.getJob())
                .build();
    }
}