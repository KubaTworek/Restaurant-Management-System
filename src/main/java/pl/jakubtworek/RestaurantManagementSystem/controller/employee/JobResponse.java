package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private UUID id;

    @NotNull(message = "Job name cannot be null.")
    private String name;
}