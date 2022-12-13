package pl.jakubtworek.RestaurantManagementSystem.controller.user;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {
    private UUID id;
    private String username;
    protected static UserResponse addLinkToResponse(UserResponse response){
        response.add(WebMvcLinkBuilder.linkTo(UserController.class).slash(response.getId()).withSelfRel());
        return response;
    }
}
