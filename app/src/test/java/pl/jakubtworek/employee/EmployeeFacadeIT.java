package pl.jakubtworek.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.Job;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeFacadeIT {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeQueryRepository employeeQueryRepository;
    @Mock
    private DomainEventPublisher publisher;

    private EmployeeFacade employeeFacade;

    private final EmployeeDto employeeDto = EmployeeDto.create(1L, "John", "Doe", Job.COOK, Status.ACTIVE);
    private final Employee employee = createEmployee(1L, "John", "Doe", Job.COOK);
    private final Set<EmployeeDto> employees = new HashSet<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeFacade = new EmployeeFacade(
                employeeRepository,
                employeeQueryRepository,
                publisher
        );
        when(employeeQueryRepository.findDtoById(1L)).thenReturn(Optional.of(employeeDto));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeQueryRepository.findDtoByStatus(Status.ACTIVE)).thenReturn(employees);
        when(employeeQueryRepository.findDtoById(1L)).thenReturn(Optional.of(employeeDto));
        when(employeeQueryRepository.findByJob(Job.COOK)).thenReturn(employees);
    }

    @Test
    void shouldSaveEmployeeAndAddToQueue() {
        // given
        final var request = new EmployeeRequest("John", "Doe", "COOK");

        // when
        final var result = employeeFacade.save(request);

        // then
        assertEquals(employeeDto.getId(), result.getId());
        assertEquals(employeeDto.getFirstName(), result.getFirstName());
        assertEquals(employeeDto.getLastName(), result.getLastName());
        assertEquals(employeeDto.getJob(), result.getJob());
        assertEquals(employeeDto.getStatus(), result.getStatus());
        verify(publisher).publish(any(EmployeeEvent.class));
    }

    @Test
    void shouldDeleteEmployee() {
        // when
        employeeFacade.deleteById(1L);

        // then
        verify(employeeRepository).deactivateEmployee(1L);
    }

    @Test
    void shouldFindAllEmployees() {
        // when
        final var result = new HashSet<>(employeeFacade.findAll());

        // then
        assertEquals(employees, result);
    }

    @Test
    void shouldFindEmployeeById() {
        // when
        final var result = employeeFacade.findById(1L);

        // then
        assertEquals(Optional.of(employeeDto), result);
    }

    @Test
    void shouldFindEmployeeByJob() {
        // when
        final var result = employeeFacade.findByJob("COOK");

        // then
        assertEquals(employees.size(), result.size());
    }

    private Employee createEmployee(Long id, String firstName, String lastName, Job job) {
        return Employee.restore(new EmployeeSnapshot(
                id, firstName, lastName, job, Status.ACTIVE, new HashSet<>()
        ));
    }
}
