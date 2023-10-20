package pl.jakubtworek.employee.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = EmployeeDto.DeserializationImpl.class)
public interface EmployeeDto {

    static EmployeeDto create(final Long id, final String firstName, final String lastName, final Job job) {
        return new EmployeeDto.DeserializationImpl(id, firstName, lastName, job);
    }

    Long getId();

    String getFirstName();

    String getLastName();

    Job getJob();

    class DeserializationImpl implements EmployeeDto {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final Job job;

        DeserializationImpl(final Long id, final String firstName, final String lastName, final Job job) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.job = job;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getFirstName() {
            return firstName;
        }

        @Override
        public String getLastName() {
            return lastName;
        }

        @Override
        public Job getJob() {
            return job;
        }
    }
}
