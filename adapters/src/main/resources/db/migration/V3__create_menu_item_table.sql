CREATE TABLE MENU_ITEMS
(
    MENU_ITEM_ID   BIGINT AUTO_INCREMENT,
    MENU_ITEM_NAME VARCHAR(255)   NOT NULL,
    PRICE          DECIMAL(19, 4) NOT NULL,
    STATUS         VARCHAR(8)     NOT NULL,
    MENU_ID        BIGINT         NOT NULL,
    PRIMARY KEY (MENU_ITEM_ID)
);

CREATE TABLE MENU
(
    MENU_ID   BIGINT AUTO_INCREMENT,
    MENU_NAME VARCHAR(255) NOT NULL,
    PRIMARY KEY (MENU_ID)
);

ALTER TABLE MENU_ITEMS
    ADD CONSTRAINT FK_MENU_ITEMS__MENU_ID_TO_MENU FOREIGN KEY (MENU_ID) REFERENCES MENU (MENU_ID);

ALTER TABLE MENU
    ADD CONSTRAINT UK_MENU__MENU_NAME UNIQUE (MENU_NAME);