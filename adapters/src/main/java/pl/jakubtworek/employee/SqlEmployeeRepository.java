package pl.jakubtworek.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlEmployeeRepository extends JpaRepository<EmployeeSnapshot, Long> {
    Optional<EmployeeSnapshot> findById(Long id);

    <S extends EmployeeSnapshot> S save(S entity);

    void deleteById(Long id);
}

interface SqlEmployeeQueryRepository extends EmployeeQueryRepository, JpaRepository<EmployeeSnapshot, Long> {
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

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
