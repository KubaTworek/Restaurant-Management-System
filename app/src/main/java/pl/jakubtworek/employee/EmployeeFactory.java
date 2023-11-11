package pl.jakubtworek.employee;

class EmployeeFactory {
    public static Employee createEmployee(String firstName, String lastName, String job) {
        Employee employee = new Employee();
        employee.updateInfo(firstName, lastName, job);
        return employee;
    }
}
