package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderController;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse extends RepresentationModel<MenuResponse> {
    private UUID id;
    private String name;
    private List<MenuItemResponse> menuItems;

    protected static MenuResponse addLinkToResponse(MenuResponse response){
        response.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash(response.getId()).withSelfRel());
        if(response.getMenuItems() != null){
            for(MenuItemResponse mi: response.getMenuItems()){
                mi.add(WebMvcLinkBuilder.linkTo(MenuItemController.class).slash(mi.getId()).withSelfRel());
            }
        }
        return response;
    }
}