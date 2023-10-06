package pl.jakubtworek.restaurant.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByJobName(String jobName);
}