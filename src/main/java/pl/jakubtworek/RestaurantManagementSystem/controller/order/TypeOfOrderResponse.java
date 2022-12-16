package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfOrderResponse {
    private UUID id;
    private String type;
}