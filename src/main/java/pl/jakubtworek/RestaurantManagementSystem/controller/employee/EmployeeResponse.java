package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private JobResponse job;
}