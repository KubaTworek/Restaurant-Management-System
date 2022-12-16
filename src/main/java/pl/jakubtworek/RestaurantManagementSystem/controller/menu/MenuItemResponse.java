package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse extends RepresentationModel<MenuItemResponse> {
    private UUID id;
    private String name;
    private double price;

    protected static MenuItemResponse addLinkToResponse(MenuItemResponse response) {
        response.add(WebMvcLinkBuilder.linkTo(MenuItemController.class).slash(response.getId()).withSelfRel());
        return response;
    }
}