package pl.jakubtworek.employee.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.order.dto.Status;

@JsonDeserialize(as = EmployeeDto.DeserializationImpl.class)
public interface EmployeeDto {

    static EmployeeDto create(final Long id,
                              final String firstName,
                              final String lastName,
                              final Job job,
                              final Status status
    ) {
        return new EmployeeDto.DeserializationImpl(id, firstName, lastName, job, status);
    }

    Long getId();

    String getFirstName();

    String getLastName();

    Job getJob();

    Status getStatus();

    class DeserializationImpl implements EmployeeDto {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final Job job;
        private final Status status;

        DeserializationImpl(final Long id,
                            final String firstName,
                            final String lastName,
                            final Job job,
                            final Status status
        ) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.job = job;
            this.status = status;
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

        @Override
        public Status getStatus() {
            return status;
        }
    }
}
