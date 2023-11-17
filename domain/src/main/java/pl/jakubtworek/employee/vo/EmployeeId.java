package pl.jakubtworek.employee.vo;

public class EmployeeId {
    private Long id;

    EmployeeId() {
    }

    public EmployeeId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
