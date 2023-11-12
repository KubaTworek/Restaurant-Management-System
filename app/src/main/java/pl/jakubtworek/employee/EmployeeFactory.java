package pl.jakubtworek.employee;

class EmployeeFactory {
    static Employee createEmployee(String firstName, String lastName, String job) {
        Employee employee = new Employee();
        employee.updateInfo(firstName, lastName, job);
        return employee;
    }
}
