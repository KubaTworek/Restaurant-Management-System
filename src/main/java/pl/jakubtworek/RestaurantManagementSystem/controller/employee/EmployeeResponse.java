package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderController;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse extends RepresentationModel<EmployeeResponse> {
    private UUID id;
    private String firstName;
    private String lastName;
    private JobResponse job;

    protected static EmployeeResponse addLinkToResponse(EmployeeResponse response){
        response.add(WebMvcLinkBuilder.linkTo(EmployeeResponse.class).slash(response.getId()).withSelfRel());
        return response;
    }
}