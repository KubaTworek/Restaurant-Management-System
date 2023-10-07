package pl.jakubtworek.employee.dto;

public class EmployeeDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Job job;

    EmployeeDto() {
    }

    EmployeeDto(final Long id, final String firstName, final String lastName, final Job job) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

    static public Builder builder() {
        return new Builder();
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

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private Job job;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withJob(Job job) {
            this.job = job;
            return this;
        }

        public EmployeeDto build() {
            return new EmployeeDto(id, firstName, lastName, job);
        }
    }
}
