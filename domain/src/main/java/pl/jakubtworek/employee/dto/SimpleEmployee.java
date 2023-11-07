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

    public static SimpleEmployee restore(final SimpleEmployeeSnapshot snapshot) {
        return new SimpleEmployee(snapshot.getId(), snapshot.getFirstName(), snapshot.getLastName(), snapshot.getJob());
    }

    public SimpleEmployeeSnapshot getSnapshot() {
        return new SimpleEmployeeSnapshot(id, firstName, lastName, job);
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
