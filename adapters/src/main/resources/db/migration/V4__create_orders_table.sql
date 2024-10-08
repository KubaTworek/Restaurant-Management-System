CREATE TABLE ORDERS
(
    ORDER_ID          BIGINT AUTO_INCREMENT,
    HOUR_ORDER        timestamp  default sysdate,
    HOUR_PREPARED     timestamp  default NULL,
    HOUR_RECEIVED     timestamp  default NULL,
    TYPE_OF_ORDER     VARCHAR(9)     NOT NULL,
    STATUS            VARCHAR(30)    NOT NULL,
    CUSTOMER_ID       BIGINT         NOT NULL,
    ORDER_DELIVERY_ID BIGINT     default NULL,
    PRIMARY KEY (ORDER_ID),
    CONSTRAINT FK_ORDER_DELIVERY_ID FOREIGN KEY (ORDER_DELIVERY_ID) REFERENCES ORDER_DELIVERY (ORDER_DELIVERY_ID)
);

CREATE TABLE ORDER_PRICE
(
    ORDER_PRICE_ID     BIGINT,
    PRICE              DECIMAL(19, 4) NOT NULL,
    DELIVER_FEE        DECIMAL(19, 4),
    MINIMUM_BASKET_FEE DECIMAL(19, 4),
    TIP                DECIMAL(19, 4),
    ORDER_ID           BIGINT,
    PRIMARY KEY (ORDER_PRICE_ID),
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID)
);

CREATE TABLE ORDER_DELIVERY
(
    ORDER_DELIVERY_ID BIGINT,
    HOUR_START        timestamp default NULL,
    HOUR_END          timestamp default NULL,
    STATUS            VARCHAR(10),
    DISTRICT          VARCHAR(255),
    STREET            VARCHAR(255),
    HOUSE_NUMBER      VARCHAR(255),
    ORDER_ID          BIGINT,
    EMPLOYEE_ID       BIGINT,
    PRIMARY KEY (ORDER_DELIVERY_ID),
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID),
    CONSTRAINT FK_EMPLOYEE_ID FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID)
);

CREATE TABLE ORDER_ITEMS
(
    ORDER_ITEM_ID   BIGINT,
    ORDER_ITEM_NAME VARCHAR(255)   NOT NULL,
    PRICE           DECIMAL(19, 4) NOT NULL,
    AMOUNT          INTEGER        NOT NULL,
    ORDER_ID        BIGINT,
    PRIMARY KEY (ORDER_ITEM_ID),
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID)
);

CREATE TABLE ORDERS__EMPLOYEE
(
    ORDER_ID    BIGINT,
    EMPLOYEE_ID BIGINT,
    CONSTRAINT FK_ORDER_ID FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ORDER_ID),
    CONSTRAINT FK_EMPLOYEE_ID FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES (EMPLOYEE_ID)
);

ALTER TABLE ORDERS
    ADD CONSTRAINT CHECK__TYPE_OF_ORDER
        CHECK (TYPE_OF_ORDER IN ('ON_SITE', 'DELIVERY'));

CREATE INDEX ORDERS__HOUR_AWAY ON ORDERS (HOUR_AWAY);
CREATE INDEX ORDERS__HOUR_AWAY ON ORDERS (TYPE_OF_ORDER);

