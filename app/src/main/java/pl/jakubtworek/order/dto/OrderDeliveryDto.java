package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.order.vo.DeliveryStatus;

import java.time.ZonedDateTime;

@JsonDeserialize(as = OrderDeliveryDto.DeserializationImpl.class)
public interface OrderDeliveryDto {
    static OrderDeliveryDto create(final Long id,
                                   final ZonedDateTime hourStart,
                                   final ZonedDateTime hourEnd,
                                   final DeliveryStatus status,
                                   final String district,
                                   final String street,
                                   final String houseNumber

    ) {
        return new OrderDeliveryDto.DeserializationImpl(id, hourStart, hourEnd, status, district, street, houseNumber);
    }

    Long getId();

    ZonedDateTime getHourStart();

    ZonedDateTime getHourEnd();

    DeliveryStatus getStatus();

    String getDistrict();

    String getStreet();

    String getHouseNumber();

    class DeserializationImpl implements OrderDeliveryDto {
        private Long id;
        private ZonedDateTime hourStart;
        private ZonedDateTime hourEnd;
        private DeliveryStatus status;
        private String district;
        private String street;
        private String houseNumber;

        DeserializationImpl(final Long id,
                            final ZonedDateTime hourStart,
                            final ZonedDateTime hourEnd,
                            final DeliveryStatus status,
                            final String district,
                            final String street,
                            final String houseNumber
        ) {
            this.id = id;
            this.hourStart = hourStart;
            this.hourEnd = hourEnd;
            this.status = status;
            this.district = district;
            this.street = street;
            this.houseNumber = houseNumber;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public ZonedDateTime getHourStart() {
            return hourStart;
        }

        @Override
        public ZonedDateTime getHourEnd() {
            return hourEnd;
        }

        @Override
        public DeliveryStatus getStatus() {
            return status;
        }

        @Override
        public String getDistrict() {
            return district;
        }

        @Override
        public String getStreet() {
            return street;
        }

        @Override
        public String getHouseNumber() {
            return houseNumber;
        }
    }
}
