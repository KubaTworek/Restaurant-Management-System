package pl.jakubtworek.employee.dto;

public class SimpleEmployee {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;

    public SimpleEmployee(final Long id, final String firstName, final String lastName, final Job job) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
