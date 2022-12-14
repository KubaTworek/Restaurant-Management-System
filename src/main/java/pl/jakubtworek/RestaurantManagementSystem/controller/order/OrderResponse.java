package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserResponse;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends RepresentationModel<OrderResponse> {
    private UUID id;
    private double price;
    private String date;
    private String hourOrder;
    private String hourAway;
    private TypeOfOrderResponse typeOfOrder;
    private List<MenuItemResponse> menuItems;
    private List<EmployeeResponse> employees;
    private UserResponse user;

    protected static OrderResponse addLinkToResponse(OrderResponse response) {
        response.add(WebMvcLinkBuilder.linkTo(OrderController.class).slash(response.getId()).withSelfRel());
        if (response.getEmployees() != null) {
            for (EmployeeResponse e : response.getEmployees()) {
                e.add(WebMvcLinkBuilder.linkTo(EmployeeController.class).slash(e.getId()).withSelfRel());
            }
        }
        if (response.getMenuItems() != null) {
            for (MenuItemResponse mi : response.getMenuItems()) {
                mi.add(WebMvcLinkBuilder.linkTo(MenuItemController.class).slash(mi.getId()).withSelfRel());
            }
        }
        return response;
    }
}