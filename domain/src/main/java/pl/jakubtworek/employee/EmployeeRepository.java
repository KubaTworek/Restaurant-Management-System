package pl.jakubtworek.employee;

import java.util.Optional;

interface EmployeeRepository {
    Optional<Employee> findById(Long id);

    Employee save(Employee entity);

    int deactivateEmployee(Long id);
}
