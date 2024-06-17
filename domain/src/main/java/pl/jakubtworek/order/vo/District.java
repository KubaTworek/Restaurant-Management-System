package pl.jakubtworek.order.vo;

import java.math.BigDecimal;

public enum District {
    SRODMIESCIE(BigDecimal.ZERO, 10),
    ZOLIBORZ(new BigDecimal("5.00"), 20),
    WOLA(new BigDecimal("7.00"), 25),
    OCHOTA(new BigDecimal("3.00"), 15),
    MOKOTOW(new BigDecimal("10.00"), 30),
    PRAGA(new BigDecimal("5.00"), 20),
    TARGOWEK(new BigDecimal("4.00"), 18),
    REMBERTOW(new BigDecimal("6.00"), 22),
    WAWER(new BigDecimal("9.00"), 28),
    WESOLA(new BigDecimal("8.00"), 26),
    WILANOW(new BigDecimal("11.00"), 35),
    URSYNOW(new BigDecimal("7.00"), 24),
    WLOCHY(new BigDecimal("5.00"), 19),
    URSUS(new BigDecimal("3.00"), 15),
    BEMOWO(new BigDecimal("4.00"), 17),
    BIELANY(new BigDecimal("6.00"), 22),
    BIALOLEKA(new BigDecimal("8.00"), 26);

    private BigDecimal deliveryFee;
    private int deliveryTime;

    District(BigDecimal deliveryFee, int deliveryTime) {
        this.deliveryFee = deliveryFee;
        this.deliveryTime = deliveryTime;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}
