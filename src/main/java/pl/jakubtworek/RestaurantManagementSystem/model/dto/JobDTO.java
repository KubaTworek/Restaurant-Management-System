package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JobDTO extends RepresentationModel<JobDTO> {

    private Long id;

    @NotNull(message = "Job name cannot be null.")
    private String name;

    private List<Employee> employees;

    public Job convertDTOToEntity() {
        return new ModelMapper().map(this, Job.class);
    }
}
