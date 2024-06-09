package pl.jakubtworek.order.vo;

public class Address {
    private String district;
    private String street;
    private String houseNumber;

    public Address(final String district, final String street, final String houseNumber) {
        this.district = district;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
}
