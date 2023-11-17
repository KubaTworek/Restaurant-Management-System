package pl.jakubtworek.common.vo;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal value) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }
}
