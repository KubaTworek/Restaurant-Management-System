-- tables
-- Table: Address
CREATE TABLE Address
(
    Id          integer NOT NULL,
    PostalCode  varchar2(6) NOT NULL,
    City        varchar2(30) NOT NULL,
    Street      varchar2(30) NOT NULL,
    HouseNumber integer NOT NULL,
    CONSTRAINT Address_pk PRIMARY KEY (Id)
);

-- Table: Customer
CREATE TABLE Customer
(
    Id         integer NOT NULL,
    Username   varchar2(40) NOT NULL,
    Passsword  varchar2(255) NOT NULL,
    Data_Id    integer NOT NULL,
    Address_Id integer NOT NULL,
    CONSTRAINT Customer_pk PRIMARY KEY (Id)
);

-- Table: Employee
CREATE TABLE Employee
(
    Id      integer NOT NULL,
    Data_Id integer NOT NULL,
    Job_Id  integer NOT NULL,
    CONSTRAINT Employee_pk PRIMARY KEY (Id)
);

-- Table: Job
CREATE TABLE Job
(
    Id   integer NOT NULL,
    Name varchar2(40) NOT NULL,
    CONSTRAINT Job_pk PRIMARY KEY (Id)
);

-- Table: Menu
CREATE TABLE Menu
(
    Id   integer NOT NULL,
    Name varchar2(40) NOT NULL,
    CONSTRAINT Menu_pk PRIMARY KEY (Id)
);

-- Table: MenuItem
CREATE TABLE MenuItem
(
    Id      integer NOT NULL,
    Name    varchar2(40) NOT NULL,
    Price   integer NOT NULL,
    Menu_Id integer NOT NULL,
    CONSTRAINT MenuItem_pk PRIMARY KEY (Id)
);

-- Table: Order
CREATE TABLE "Order"
(
    Id             integer   NOT NULL,
    Price          integer   NOT NULL,
    "Date"         date      NOT NULL,
    HourOrder      timestamp NOT NULL,
    HourAway       timestamp NOT NULL,
    TypeOfOrder_Id integer   NOT NULL,
    CONSTRAINT Order_pk PRIMARY KEY (Id)
);

-- Table: Order_Customer
CREATE TABLE Order_Customer
(
    Order_Id    integer NOT NULL,
    Customer_Id integer NOT NULL,
    CONSTRAINT Order_Customer_pk PRIMARY KEY (Order_Id, Customer_Id)
);

-- Table: Order_Employee
CREATE TABLE Order_Employee
(
    Order_Id    integer NOT NULL,
    Employee_Id integer NOT NULL,
    CONSTRAINT Order_Employee_pk PRIMARY KEY (Order_Id, Employee_Id)
);

-- Table: Order_MenuItem
CREATE TABLE Order_MenuItem
(
    Order_Id    integer NOT NULL,
    MenuItem_id integer NOT NULL,
    Amount      integer NOT NULL,
    CONSTRAINT Order_MenuItem_pk PRIMARY KEY (Order_Id, MenuItem_id)
);

-- Table: PersonalData
CREATE TABLE PersonalData
(
    Id        integer NOT NULL,
    FirstName varchar2(40) NOT NULL,
    LastName  varchar2(40) NOT NULL,
    Mail      varchar2(40) NOT NULL,
    Phone     integer NOT NULL,
    CONSTRAINT PersonalData_pk PRIMARY KEY (Id)
);

-- Table: TypeOfOrder
CREATE TABLE TypeOfOrder
(
    Id   integer NOT NULL,
    Type varchar2(20) NOT NULL,
    CONSTRAINT TypeOfOrder_pk PRIMARY KEY (Id)
);

-- foreign keys
-- Reference: Customer_Address (table: Customer)
ALTER TABLE Customer
    ADD CONSTRAINT Customer_Address
        FOREIGN KEY (Address_Id)
            REFERENCES Address (Id);

-- Reference: Customer_Data (table: Customer)
ALTER TABLE Customer
    ADD CONSTRAINT Customer_Data
        FOREIGN KEY (Data_Id)
            REFERENCES PersonalData (Id);

-- Reference: Employee_Data (table: Employee)
ALTER TABLE Employee
    ADD CONSTRAINT Employee_Data
        FOREIGN KEY (Data_Id)
            REFERENCES PersonalData (Id);

-- Reference: Employee_Job (table: Employee)
ALTER TABLE Employee
    ADD CONSTRAINT Employee_Job
        FOREIGN KEY (Job_Id)
            REFERENCES Job (Id);

-- Reference: MenuItem_Menu (table: MenuItem)
ALTER TABLE MenuItem
    ADD CONSTRAINT MenuItem_Menu
        FOREIGN KEY (Menu_Id)
            REFERENCES Menu (Id);

-- Reference: Order_Customer_Customer (table: Order_Customer)
ALTER TABLE Order_Customer
    ADD CONSTRAINT Order_Customer_Customer
        FOREIGN KEY (Customer_Id)
            REFERENCES Customer (Id);

-- Reference: Order_Customer_Order (table: Order_Customer)
ALTER TABLE Order_Customer
    ADD CONSTRAINT Order_Customer_Order
        FOREIGN KEY (Order_Id)
            REFERENCES "Order" (Id);

-- Reference: Order_Employee_Employee (table: Order_Employee)
ALTER TABLE Order_Employee
    ADD CONSTRAINT Order_Employee_Employee
        FOREIGN KEY (Employee_Id)
            REFERENCES Employee (Id);

-- Reference: Order_Employee_Order (table: Order_Employee)
ALTER TABLE Order_Employee
    ADD CONSTRAINT Order_Employee_Order
        FOREIGN KEY (Order_Id)
            REFERENCES "Order" (Id);

-- Reference: Order_MenuItem_MenuItem (table: Order_MenuItem)
ALTER TABLE Order_MenuItem
    ADD CONSTRAINT Order_MenuItem_MenuItem
        FOREIGN KEY (MenuItem_id)
            REFERENCES MenuItem (Id);

-- Reference: Order_MenuItem_Order (table: Order_MenuItem)
ALTER TABLE Order_MenuItem
    ADD CONSTRAINT Order_MenuItem_Order
        FOREIGN KEY (Order_Id)
            REFERENCES "Order" (Id);

-- Reference: Order_TypeOfOrder (table: Order)
ALTER TABLE "Order"
    ADD CONSTRAINT Order_TypeOfOrder
        FOREIGN KEY (TypeOfOrder_Id)
            REFERENCES TypeOfOrder (Id);

-- End of file.

