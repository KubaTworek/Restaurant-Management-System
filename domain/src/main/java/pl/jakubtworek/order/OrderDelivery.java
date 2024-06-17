package pl.jakubtworek.order;

import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.Address;
import pl.jakubtworek.order.vo.DeliveryStatus;
import pl.jakubtworek.order.vo.District;

import java.time.ZonedDateTime;

class OrderDelivery {
    private Long id;
    private ZonedDateTime hourStart;
    private ZonedDateTime hourEnd;
    private DeliveryStatus status;
    private Address address;
    private EmployeeId employee;

    OrderDelivery() {
    }

    private OrderDelivery(final Long id,
                          final ZonedDateTime hourStart,
                          final ZonedDateTime hourEnd,
                          final DeliveryStatus status,
                          final Address address,
                          final EmployeeId employee
    ) {
        this.id = id;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.status = status;
        this.address = address;
        this.employee = employee;
    }

    static OrderDelivery restore(OrderDeliverySnapshot snapshot) {
        return new OrderDelivery(
                snapshot.getId(),
                snapshot.getHourStart(),
                snapshot.getHourEnd(),
                snapshot.getStatus(),
                new Address(snapshot.getDistrict(), snapshot.getStreet(), snapshot.getHouseNumber()),
                snapshot.getEmployee()
        );
    }

    OrderDeliverySnapshot getSnapshot() {
        return new OrderDeliverySnapshot(
                id,
                hourStart,
                hourEnd,
                status,
                address.getDistrict(),
                address.getStreet(),
                address.getHouseNumber(),
                employee
        );
    }

    void setEmployee(final EmployeeId employee) {
        this.employee = employee;
    }

    void setAddress(final Address address) {
        this.address = address;
    }

    void start() {
        this.status = DeliveryStatus.START;
        this.hourStart = ZonedDateTime.now();
    }

    void end() {
        this.status = DeliveryStatus.END;
        this.hourEnd = ZonedDateTime.now();
    }

    District getDistrict() {
        return address.getDistrict();
    }
}
