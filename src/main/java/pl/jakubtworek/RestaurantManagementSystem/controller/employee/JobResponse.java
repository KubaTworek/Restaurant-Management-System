package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private Long id;

    @NotNull(message = "Job name cannot be null.")
    private String name;
}