package pl.jakubtworek.employee;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.queue.EmployeeQueueFacade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

class EmployeeFacadeTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeQueryRepository employeeQueryRepository;
    @Mock
    private EmployeeQueueFacade employeeQueueFacade;

    private EmployeeFacade employeeFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeFacade = new EmployeeFacade(employeeRepository, employeeQueryRepository, employeeQueueFacade);
    }

    @Test
    void testGetById() {
        // given
        final var employeeId = 1L;
        final var simpleEmployee = new SimpleEmployee(employeeId, "John", "Doe", Job.COOK);
        when(employeeQueryRepository.findSimpleById(employeeId)).thenReturn(Optional.of(simpleEmployee));

        // when
        final SimpleEmployee result = employeeFacade.getById(employeeId);

        // then
        assertEquals(simpleEmployee, result);
    }

    @Test
    void testSave() {
        // given
        final var employeeRequest = new EmployeeRequest("John", "Doe", "COOK");
        final var simpleEmployee = new SimpleEmployee(1L, "John", "Doe", Job.COOK);
        final var savedEmployee = createEmployee(1L, "John", "Doe", Job.COOK);
        when(employeeRepository.save(any())).thenReturn(savedEmployee);

        // when
        final EmployeeDto result = employeeFacade.save(employeeRequest);

        // then
        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(savedEmployee.getFirstName(), result.getFirstName());
        assertEquals(savedEmployee.getLastName(), result.getLastName());
        assertEquals(savedEmployee.getJob(), result.getJob());
        verify(employeeQueueFacade).addEmployeeToProperQueue(argThat(simpleEmployeeMatcher(simpleEmployee)));
    }

    @Test
    void testDeleteById() {
        // given
        final var employeeId = 1L;

        // when
        employeeFacade.deleteById(employeeId);

        // then
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void testFindAll() {
        // given
        final Set<EmployeeDto> employeeList = new HashSet<>();
        when(employeeQueryRepository.findBy(EmployeeDto.class)).thenReturn(employeeList);

        // when
        final Set<EmployeeDto> result = new HashSet<>(employeeFacade.findAll());

        // then
        assertEquals(employeeList, result);
    }

    @Test
    void testFindById() {
        // given
        final var employeeId = 1L;
        final var employeeDto = EmployeeDto.create(1L, "John", "Doe", Job.COOK);
        when(employeeQueryRepository.findDtoById(employeeId)).thenReturn(Optional.of(employeeDto));

        // when
        final Optional<EmployeeDto> result = employeeFacade.findById(employeeId);

        // then
        assertEquals(Optional.of(employeeDto), result);
    }

    @Test
    void testFindByJobWhenJobExists() {
        // given
        final var jobName = "COOK";
        final var job = Job.COOK;
        final List<EmployeeDto> employeeList = new ArrayList<>();
        when(employeeQueryRepository.findByJob(job)).thenReturn(employeeList);

        // when
        final List<EmployeeDto> result = employeeFacade.findByJob(jobName);

        // then
        assertEquals(employeeList, result);
    }

    @Test
    void testFindByJobWhenJobDoesNotExist() {
        // given
        final var jobName = "INVALID_JOB";

        // when
        assertThrows(IllegalStateException.class,
                () -> employeeFacade.findByJob(jobName));
    }

    private Employee createEmployee(Long id, String firstName, String lastName, Job job) {
        final var employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setJob(job);
        return employee;
    }

    private Matcher<SimpleEmployee> simpleEmployeeMatcher(SimpleEmployee expected) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(SimpleEmployee item) {
                return item.getId().equals(expected.getId()) &&
                        item.getFirstName().equals(expected.getFirstName()) &&
                        item.getLastName().equals(expected.getLastName()) &&
                        item.getJob() == expected.getJob();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected SimpleEmployee fields should match");
            }
        };
    }
}
