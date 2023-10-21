package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.employee.dto.EmployeeDto;

import java.time.ZonedDateTime;
import java.util.List;

@JsonDeserialize(as = OrderDto.DeserializationImpl.class)
public interface OrderDto {

    static OrderDto create(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, List<EmployeeDto> employees) {
        return new OrderDto.DeserializationImpl(id, price, hourOrder, hourAway, typeOfOrder, employees);
    }

    Long getId();

    int getPrice();

    ZonedDateTime getHourOrder();

    ZonedDateTime getHourAway();

    TypeOfOrder getTypeOfOrder();

    List<EmployeeDto> getEmployees();

    class DeserializationImpl implements OrderDto {
        private final Long id;
        private final int price;
        private final ZonedDateTime hourOrder;
        private final ZonedDateTime hourAway;
        private final TypeOfOrder typeOfOrder;
        private final List<EmployeeDto> employees;

        DeserializationImpl(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, List<EmployeeDto> employees) {
            this.id = id;
            this.price = price;
            this.hourOrder = hourOrder;
            this.hourAway = hourAway;
            this.typeOfOrder = typeOfOrder;
            this.employees = employees;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public int getPrice() {
            return price;
        }

        @Override
        public ZonedDateTime getHourOrder() {
            return hourOrder;
        }

        @Override
        public ZonedDateTime getHourAway() {
            return hourAway;
        }

        @Override
        public TypeOfOrder getTypeOfOrder() {
            return typeOfOrder;
        }

        @Override
        public List<EmployeeDto> getEmployees() {
            return employees;
        }
    }
}
