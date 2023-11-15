package pl.jakubtworek.order.dto;

import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

public record OrderResponse(
        Long id,
        BigDecimal price,
        ZonedDateTime hourOrder,
        ZonedDateTime hourAway,
        TypeOfOrder typeOfOrder,
        Set<MenuItemDto> menuItems,
        Set<EmployeeDto> employees
) {
}
