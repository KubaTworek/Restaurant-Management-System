package pl.jakubtworek.order.vo;

public class Address {
    private District district;
    private String street;
    private String houseNumber;

    public Address(final District district, final String street, final String houseNumber) {
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Address(final String district, final String street, final String houseNumber) {
        this.district = District.valueOf(district);
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public District getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
}
