package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfOrderResponse {

    private UUID id;

    @NotNull(message = "Type of order cannot be null.")
    private String type;
}