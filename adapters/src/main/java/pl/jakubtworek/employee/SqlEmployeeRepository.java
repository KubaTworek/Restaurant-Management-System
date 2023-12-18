package pl.jakubtworek.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.Job;

import java.util.Optional;
import java.util.Set;

interface SqlEmployeeRepository extends JpaRepository<EmployeeSnapshot, Long> {
    @Query("SELECT e FROM EmployeeSnapshot e " +
            "LEFT JOIN FETCH e.orders " +
            "WHERE e.id = :id")
    Optional<EmployeeSnapshot> findById(Long id);
}

interface SqlEmployeeQueryRepository extends EmployeeQueryRepository, JpaRepository<EmployeeSnapshot, Long> {

    @Query("SELECT e FROM EmployeeSnapshot e WHERE e.id = :id")
    Optional<EmployeeDto> findDtoById(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM EmployeeSnapshot e " +
            "WHERE " +
            "(:job IS NULL OR e.job = :job)" +
            "AND (:status IS NULL OR e.status = :status)")
    Set<EmployeeDto> findFilteredEmployees(@Param("job") Job job, @Param("status") Status status);
}

@Repository
class EmployeeRepositoryImpl implements EmployeeRepository {

    private final SqlEmployeeRepository repository;

    EmployeeRepositoryImpl(final SqlEmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Employee> findById(final Long id) {
        return repository.findById(id).map(Employee::restore);
    }

    @Override
    public Employee save(final Employee entity) {
        return Employee.restore(repository.save(entity.getSnapshot()));
    }
}
