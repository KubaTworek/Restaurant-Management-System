package pl.jakubtworek.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlEmployeeRepository extends JpaRepository<SqlEmployee, Long> {
    Optional<SqlEmployee> findById(Long id);

    <S extends SqlEmployee> S save(S entity);

    void deleteById(Long id);
}

interface SqlEmployeeQueryRepository extends EmployeeQueryRepository, JpaRepository<SqlEmployee, Long> {
}

@Repository
class EmployeeRepositoryImpl implements EmployeeRepository {

    private final SqlEmployeeRepository repository;

    EmployeeRepositoryImpl(final SqlEmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Employee> findById(final Long id) {
        return repository.findById(id).map(SqlEmployee::toEmployee);
    }

    @Override
    public Employee save(final Employee entity) {
        return repository.save(SqlEmployee.fromEmployee(entity)).toEmployee();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}