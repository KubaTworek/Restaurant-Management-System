package pl.jakubtworek.order;

import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.DeliveryStatus;
import pl.jakubtworek.order.vo.District;

import java.time.ZonedDateTime;

class OrderDeliverySnapshot {
    private Long id;
    private ZonedDateTime hourStart;
    private ZonedDateTime hourEnd;
    private DeliveryStatus status;
    private District district;
    private String street;
    private String houseNumber;
    private EmployeeId employee;

    OrderDeliverySnapshot() {
    }

    OrderDeliverySnapshot(final Long id,
                          final ZonedDateTime hourStart,
                          final ZonedDateTime hourEnd,
                          final DeliveryStatus status,
                          final District district,
                          final String street,
                          final String houseNumber,
                          final EmployeeId employee
    ) {
        this.id = id;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.status = status;
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
        this.employee = employee;
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

    District getDistrict() {
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
}
