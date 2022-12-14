package pl.jakubtworek.RestaurantManagementSystem.security.config;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

public class H2DialectCustom extends H2Dialect {

    public H2DialectCustom() {
        super();
        registerColumnType(Types.BINARY, "varbinary");
    }

}