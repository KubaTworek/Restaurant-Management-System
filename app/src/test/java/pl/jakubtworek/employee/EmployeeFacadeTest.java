package pl.jakubtworek.employee;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    void shouldReturnEmployeeById() {
        // given
        final var employeeId = 1L;
        final var expectedEmployee = new SimpleEmployee(employeeId, "John", "Doe", Job.COOK);

        when(employeeQueryRepository.findSimpleById(employeeId)).thenReturn(Optional.of(expectedEmployee));

        // when
        final SimpleEmployee result = employeeFacade.getById(employeeId);

        // then
        assertEquals(expectedEmployee, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOK", "DELIVERY", "WAITER"})
    void shouldSaveEmployeeAndAddToQueue(String jobName) {
        // given
        final var job = Job.valueOf(jobName);
        final var request = new EmployeeRequest("John", "Doe", jobName);
        final var expectedSimpleEmployee = new SimpleEmployee(1L, "John", "Doe", job);
        final var expectedEmployee = createEmployee(1L, "John", "Doe", job);

        when(employeeRepository.save(any())).thenReturn(expectedEmployee);

        // when
        final EmployeeDto result = employeeFacade.save(request);

        // then
        assertEmployeeEquals(expectedEmployee, result);
        verify(employeeQueueFacade).addToQueue(argThat(simpleEmployeeMatcher(expectedSimpleEmployee)));
    }

    @Test
    void shouldDeleteEmployee() {
        // given
        final var employeeId = 1L;

        // when
        employeeFacade.deleteById(employeeId);

        // then
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void shouldFindAllEmployees() {
        // given
        final Set<EmployeeDto> expectedEmployees = new HashSet<>();

        when(employeeQueryRepository.findBy(EmployeeDto.class)).thenReturn(expectedEmployees);

        // when
        final Set<EmployeeDto> result = new HashSet<>(employeeFacade.findAll());

        // then
        assertEquals(expectedEmployees, result);
    }

    @Test
    void shouldFindEmployeeById() {
        // given
        final var employeeId = 1L;
        final var expectedEmployee = EmployeeDto.create(1L, "John", "Doe", Job.COOK);

        when(employeeQueryRepository.findDtoById(employeeId)).thenReturn(Optional.of(expectedEmployee));

        // when
        final Optional<EmployeeDto> result = employeeFacade.findById(employeeId);

        // then
        assertEquals(Optional.of(expectedEmployee), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOK", "DELIVERY", "WAITER"})
    void shouldFindEmployeeByJob(String jobName) {
        // given
        final var job = Job.valueOf(jobName);
        final List<EmployeeDto> expectedEmployees = new ArrayList<>();

        when(employeeQueryRepository.findByJob(job)).thenReturn(expectedEmployees);

        // when
        List<EmployeeDto> result = employeeFacade.findByJob(jobName);

        // then
        assertEquals(expectedEmployees, result);
    }

    private Employee createEmployee(Long id, String firstName, String lastName, Job job) {
        return Employee.restore(new EmployeeSnapshot(
                id, firstName, lastName, job, new HashSet<>()
        ));
    }

    private void assertEmployeeEquals(Employee expected, EmployeeDto actual) {
        assertEquals(expected.getSnapshot().getId(), actual.getId());
        assertEquals(expected.getSnapshot().getFirstName(), actual.getFirstName());
        assertEquals(expected.getSnapshot().getLastName(), actual.getLastName());
        assertEquals(expected.getSnapshot().getJob(), actual.getJob());
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
