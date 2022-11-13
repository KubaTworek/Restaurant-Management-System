package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TypeOfOrderResponse {

    private Long id;

    @NotNull(message = "Type of order cannot be null.")
    private String type;
}