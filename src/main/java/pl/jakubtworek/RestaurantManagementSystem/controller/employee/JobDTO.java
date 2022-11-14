package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private Long id;

    @NotNull(message = "Job name cannot be null.")
    private String name;

    public Job convertDTOToEntity() {
        return new ModelMapper().map(this, Job.class);
    }
}