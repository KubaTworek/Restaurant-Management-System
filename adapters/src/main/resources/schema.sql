CREATE TABLE IF NOT EXISTS users
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    username
    VARCHAR
(
    255
) NOT NULL,
    password VARCHAR
(
    255
)
    );

CREATE TABLE IF NOT EXISTS employee
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    first_name
    VARCHAR
(
    255
),
    last_name VARCHAR
(
    255
),
    job VARCHAR
(
    255
)
    );

CREATE TABLE IF NOT EXISTS menu
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL
    );

CREATE TABLE IF NOT EXISTS menu_item
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
),
    price INT,
    menu_id INT,
    FOREIGN KEY
(
    menu_id
) REFERENCES menu
(
    id
)
    );

CREATE TABLE IF NOT EXISTS orders
(
    id
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    price
    INT,
    hour_order
    TIMESTAMP,
    hour_away
    TIMESTAMP,
    type_of_order
    VARCHAR
(
    255
),
    user_id INT,
    FOREIGN KEY
(
    user_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS Order_Menu_Item
(
    order_id
    INT,
    menu_item_id
    INT,
    PRIMARY
    KEY
(
    order_id,
    menu_item_id
),
    FOREIGN KEY
(
    order_id
) REFERENCES orders
(
    id
),
    FOREIGN KEY
(
    menu_item_id
) REFERENCES menu_item
(
    id
)
    );

CREATE TABLE IF NOT EXISTS Order_Employee
(
    order_id
    INT,
    employee_id
    INT,
    PRIMARY
    KEY
(
    order_id,
    employee_id
),
    FOREIGN KEY
(
    order_id
) REFERENCES orders
(
    id
),
    FOREIGN KEY
(
    employee_id
) REFERENCES employee
(
    id
)
    );
