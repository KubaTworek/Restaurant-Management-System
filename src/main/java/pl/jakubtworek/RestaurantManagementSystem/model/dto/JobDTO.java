package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.JobResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private Long id;
    private String name;
    private List<Employee> employees;

    public Job convertDTOToEntity() {
        return new ModelMapper().map(this, Job.class);
    }
    public JobResponse convertDTOToResponse() {
        return new ModelMapper().map(this, JobResponse.class);
    }
}