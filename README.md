# Restaurant Management System

> The application is designed for restaurant management. It allows adding, deleting, and editing menu items as well as managing staff members. Additionally, users can create orders that will be fulfilled by specific employees.
>
> The application is a modular monolith implemented in Java and Spring, utilizing an H2 database stored in a file. During the project's development, particular emphasis was placed on its architectural design. The process began with the construction of ports and adapters, and the core of the application adheres to the principles of Domain-Driven Design (DDD). The project has been crafted in accordance with Clean Code principles and adheres to good object-oriented programming practices. Comprehensive unit and integration tests have been conducted to validate all classes and methods.

## Table of Contents

* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Run and Test](#run-and-test)
* [Endpoints](#endpoints)
* [Database](#database)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Contact](#contact)

## General Information

### Menu Management:

Users can add, remove, and modify menu items.
Each menu item is associated with details such as name, price, and status.

### Employee Management:

The application allows the administration of the staff, including adding and removing employees.
Staff members are categorized based on their roles and responsibilities.

### Order Processing:

Users can create orders, specifying the type of order (e.g., dine-in, takeout) and selecting items from the menu.
Orders are processed by assigning them to specific employees for fulfillment.

### Architecture and Technology:

The application follows a modular monolith architecture written in Java and utilizes the Spring framework.
The data is stored in an H2 database, and the information persists in a file.

### Domain-Driven Design (DDD):

The core of the application is designed according to the principles of DDD, emphasizing a domain-centric approach to modeling.

### Testing:

Comprehensive testing practices, including unit and integration tests, have been implemented to ensure the reliability and correctness of the code.

### Clean Code Principles:

The project adheres to Clean Code principles, promoting readability, maintainability, and extensibility.

### Generics:

The project was initially written in early 2022 as a simple console application as part of a university assignment. Since then, I have been consistently working on this project, testing and incorporating newly acquired software development methods.

## Technologies Used

- Java 17
- Spring Boot
- Hibernate, JPA
- H2, Flyway

## Run and Test

To run the application type

```
mvn spring-boot:run
```

To execute unit and integration tests

```
mvn test
```

## Endpoints

This API provides HTTP endpoint's and tools for the following:

# Employee

* Create an employee: `POST /employees/{jobName}`
* Delete an employee (by id): `DELETE /employees/{employeeId}`
* Find all employees in restaurant: `GET /employees`
* Find unique employee in restaurant (by id): `GET /employees/{employeeId}`
* Find employees in restaurant by param (jobName) : `GET /employees?job={jobName}`

Request Body [Employee]

```
{
     "firstName": varchar(50),
     "lastName": varchar(50),
     "job": varchar[COOK/WAITER/DELIVERY]
}
```

Response Body [Employee]

```
{
     "id": bigint
     "firstName": varchar(50),
     "lastName": varchar(50),
     "job": varchar[COOK/WAITER/DELIVERY]
     "status": varchar[ACTIVE/INACTIVE]
}
```

# Menu

* Create a menu item: `POST /menu-items`
* Find every menu in restaurant: `GET /menu-items`
* Delete a menu item (by id): `DELETE /menu-items/{menuItemId}`
* Find unique menu item in restaurant (by id): `GET/menu-items/{menuItemId}`
* Find menu items in restaurant (by menu name): `GET/menu-items/menu/{menuName}`

Request Body [MenuItem]

```
{
     "name": varchar(20),
     "price": decimal,
     "menu": varchar(20)
}
```

Response Body [Menu]

```
{
     "id": bigint,
     "name": varchar(20),
     "menuItems": []
}
```

Response Body [Menu-Item]

```
{
     "id": bigint,
     "name": varchar(20),
     "price": decimal,
     "status": varchar[ACTIVE/INACTIVE]
}
```

# Order

* Create an order: `POST /orders`

Request Header:
Authorization: Bearer <jwt-token>

* Find orders in restaurant assigned to you: `GET /orders`

Request Header:
Authorization: Bearer <jwt-token>

* Find unique order in restaurant (by id): `GET/orders/{orderId}`
* Find made orders in restaurant: `GET /orders/filter`

Request Parameters:

- fromDate: Start date for filtering (optional)
- toDate: End date for filtering (optional)
- typeOfOrder: Type of order for filtering (optional)
- isReady: Filter orders that are ready (optional)
- employeeId: Filter orders by employee ID (optional)
- userId: Filter orders by user ID (optional)

Request Body [Order]

```
{
     "typeOfOrder": varchar[TAKE_AWAY/ON_SITE/DELIVERY],
     "menuItems": [varchar(40)...]
}
```

Response Body [Order]

```
{
     "id": bigint,
     "price": decimal,
     "hourOrder": datetime,
     "hourAway": datetime(optional),
     "typeOfOrder": varchar[TAKE_AWAY/ON_SITE/DELIVERY],
     "orderItems": [
      {
        "id": bigint,
        "name": varchar(40),
        "price": decimal,
        "amount": integer
      }...
    ]
}
```

# User

* Register user with a specific role: `POST /users/register`
* Login user: `DELETE /users/login`

Request Body [Register]

```
{
  "username": varchar(40),
  "password": varchar(40)
}
```

Response Body [Register]

```
{
     "id": bigint,
     "username": varchar(40)
}
```

Request Body [Login]

```
{
     "username": varchar(40),
     "password": varchar(40)
}
```

Response Body [Login]

```
{
     "username": varchar(40),
     "token": varchar(255),
     "tokenExpirationDate": Long
}
```

## Database

### Schema

![Database schema](database/schema.png)

### Description

The database comprises tables for managing a restaurant system. The USERS table stores user authentication data, while the EMPLOYEES table contains information about restaurant staff. MENU_ITEMS and MENU tables handle menu items and categories. ORDERS and ORDER_ITEMS tables manage customer orders and their details. The ORDERS__EMPLOYEE table establishes relationships between orders and assigned employees. The schema supports key functionalities such as user authentication, employee management, menu item organization, and order processing in a restaurant setting. Foreign key constraints ensure data integrity, and unique constraints are applied where needed for consistency and efficiency.

## Project Status

Project is: _in_progress_

## Room for Improvement

Room for improvement:

- Add frontend based on React
- Add external tests
- Create a more complex logic that enables the management of orders by employees.

## Contact

Created by https://github.com/KubaTworek
