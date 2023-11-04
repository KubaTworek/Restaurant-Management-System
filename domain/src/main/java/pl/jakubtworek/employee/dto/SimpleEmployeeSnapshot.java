package pl.jakubtworek.employee.dto;

public class SimpleEmployeeSnapshot {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;

    public SimpleEmployeeSnapshot() {
    }

    public SimpleEmployeeSnapshot(final Long id, final String firstName, final String lastName, final Job job) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Job getJob() {
        return job;
    }
}
