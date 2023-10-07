package pl.jakubtworek.employee;

import java.util.Optional;

interface EmployeeRepository {
    Optional<Employee> findById(Long id);

    Employee save(Employee entity);

    void deleteById(Long id);
}