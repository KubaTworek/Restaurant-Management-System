package pl.jakubtworek.order;

import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.DeliveryStatus;

import java.time.ZonedDateTime;

class OrderDeliverySnapshot {
    private Long id;
    private ZonedDateTime hourStart;
    private ZonedDateTime hourEnd;
    private DeliveryStatus status;
    private String district;
    private String street;
    private String houseNumber;
    private EmployeeId employee;
    private OrderSnapshot order;

    OrderDeliverySnapshot() {
    }

    OrderDeliverySnapshot(final Long id,
                          final ZonedDateTime hourStart,
                          final ZonedDateTime hourEnd,
                          final DeliveryStatus status,
                          final String district,
                          final String street,
                          final String houseNumber,
                          final EmployeeId employee,
                          final OrderSnapshot order
    ) {
        this.id = id;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.status = status;
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
        this.employee = employee;
        this.order = order;
    }

    Long getId() {
        return id;
    }

    ZonedDateTime getHourStart() {
        return hourStart;
    }

    ZonedDateTime getHourEnd() {
        return hourEnd;
    }

    DeliveryStatus getStatus() {
        return status;
    }

    String getDistrict() {
        return district;
    }

    String getStreet() {
        return street;
    }

    String getHouseNumber() {
        return houseNumber;
    }

    EmployeeId getEmployee() {
        return employee;
    }

    OrderSnapshot getOrder() {
        return order;
    }
}
