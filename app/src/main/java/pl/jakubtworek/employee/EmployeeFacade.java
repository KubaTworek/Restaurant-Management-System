package pl.jakubtworek.employee;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.common.vo.Role;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.vo.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeFacade {
    private final UserFacade userFacade;
    private final EmployeeQueryRepository employeeQueryRepository;
    private final Employee employee;


    EmployeeFacade(final UserFacade userFacade,
                   final EmployeeQueryRepository employeeQueryRepository,
                   final Employee employee
    ) {
        this.userFacade = userFacade;
        this.employeeQueryRepository = employeeQueryRepository;
        this.employee = employee;
    }

    EmployeeDto save(EmployeeRequest toSave, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        final var created = employee.from(
                toSave.firstName(),
                toSave.lastName(),
                toSave.job()
        );
        return toDto(created);
    }

    void deactivateById(Long id, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        employee.deactivate(id);
    }

    List<EmployeeDto> findByParams(String job, String status, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        return new ArrayList<>(
                employeeQueryRepository.findFilteredEmployees(parseJob(job), parseStatus(status))
        );
    }

    Optional<EmployeeDto> findById(Long id, String jwt) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        return employeeQueryRepository.findDtoById(id);
    }

    private EmployeeDto toDto(Employee employee) {
        final var snap = employee.getSnapshot();
        return EmployeeDto.create(snap.getId(), snap.getFirstName(), snap.getLastName(), snap.getJob(), snap.getStatus());
    }

    private Status parseStatus(String status) {
        return status != null ? Status.valueOf(status) : null;
    }

    private Job parseJob(String job) {
        return job != null ? Job.valueOf(job) : null;
    }
}
