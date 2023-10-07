# Restaurant Management System

> REST APIs based on CRUD methods. The project was written in Java using Spring. The database tries to maintain a third
> normal form, it was built on an internal H2 database. The Admin position gives us the ability to manage the
> restaurant,
> modify the menu and employees, and collect information on placed orders. User privileges allow us to place orders,
> while
> we don't even need to be authenticated to view the menu. The project has been written in accordance with the
> principles
> of Clean Code and good object-oriented programming practices. All classes and methods have been tested with unit and
> integration tests.

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

- Restaurant manager gives us the ability to add, modify and delete menus and the dishes belonging to them. We also have
  the ability to manage employees such as cooks, waiters and delivery men. The customer has the ability to place orders
  on the spot and for take-out. The restaurant also has a cook and order delivery system, which is used by waiters and
  delivery personnel. A system of authentication and authorization by login data and JWT token has been introduced.


- The purpose of writing this project is to improve my programming skills, learn various tools and demonstrate my
  ability to write readable and working code.


- I prepared a project with similar requirements in my GUI classes (https://github.com/KubaTworek/Restaurant-Management)
  but I decided to develop it further as a full-fledged web application using a wide range of tools.

## Technologies Used

- Java 11
- Spring Boot
- Spring Security
- Hibernate, JPA
- Postman

## Run and Test

To run the application type

```
mvn spring-boot:run
```

To execute unit and integration tests

```
mvn test
```

To execute unit tests

```
mvn '-Dtest=*Test' test
```

To execute integration tests

```
mvn '-Dtest=*IT' test
```

To execute End-toEnd tests in Postman (need NodeJs and Newman)

```
newman run Restaurant-Management-System.postman_collection.json
```

## Endpoints

This API provides HTTP endpoint's and tools for the following:

* JWT [ADMIN] =
  eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3MDk5MDgwMSwiZXhwIjoxMDAxNjcwOTkwODAxfQ.-3BdaIuq5rSWzf9zIdkR3S1ftAk9SSGY5iaX0zCYO08


* JWT [USER] =
  eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJ1c2VyIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NzA5OTA3MTEsImV4cCI6MTAwMTY3MDk5MDcxMX0.FQXWI59l0axX413c5H4F1lByE2mSK4fe4pBvvR-gYV8

# Employee

* Create an employee: `POST/employees/{jobName}`
* Delete an employee (by id): `DELETE/employees/{employeeId}`
* Find all employees in restaurant: `GET/employees`
* Find unique employee in restaurant (by id): `GET/employees/{employeeId}`
* Find employees in restaurant by param (name) : `GET/employees`

Request Body [Employee]

```
{
     "firstName": varchar(50),
     "lastName": varchar(50),
     "job": varchar[Cook/Waiter/DeliveryMan]
}
```

# Menu

* Create a menu: `POST/menu`
* Update a menu: `PUT/menu`
* Create a menu item and add to menu (by menu name): `POST/menu-items`
* Update a menu item and add to menu (by menu name): `PUT/menu-items`
* Delete a menu (by id): `DELETE/menu/{menuId}`
* Delete a menu item (by id): `DELETE/menu-items/{menuItemId}`
* Find every menu in restaurant: `GET/menu`
* Find unique menu in restaurant (by id): `GET/menu/{menuId}`
* Find menu items in restaurant (by menu name): `GET/menu-items/menu/{menuName}`

Request Body [Menu]

```
{
     "name": varchar(20)
}
```

Request Body [Menu-Item]

```
{
     "name": varchar(20),
     "price": double,
     "menu": varchar[Menu name]
}
```

# Order

* Create an order: `POST/orders`
* Delete an order (by id): `DELETE/orders/{orderId}`
* Find every order in restaurant: `GET/orders`
* Find unique order in restaurant (by id): `GET/orders/{orderId}`
* Find made orders in restaurant: `GET/orders/ready`
* Find unmade orders in restaurant: `GET/orders/unready`

Request Body [Order]

```
{
    "typeOfOrder": varchar[On-site, Delivery]
      "menuItems": [
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    },
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    },
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    }
}
```

# Order-Admin

* Create an order: `POST/admin-orders`
* Delete an order (by id): `DELETE/admin-orders/{orderId}`
* Find all order in restaurant: `GET/admin-orders`
* Find unique order in restaurant (by id): `GET/admin-orders/{orderId}`
* Find made orders in restaurant: `GET/admin-orders/ready`
* Find unmade orders in restaurant: `GET/admin-orders/unready`
* Find orders in restaurant by params (by type of order, date, employee id): `GET/admin-orders/find`

Request Body [Order]

```
{
    "typeOfOrder": varchar[On-site, Delivery]
      "menuItems": [
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    },
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    },
    {
        "name": varchar(20),
        "price": Double,
        "menu": varchar(20)
    }
}
```

# User

* Register user with a specific role: `POST/user`
* Delete user (by username): `DELETE/users/{username}`

Request Body [User]

```
{
    "username": varchar(20),
    "password": varchar(50),
    "role": varchar[ROLE_USER, ROLE_ADMIN]
}
```

## Database

### Schema

![Database schema](database/schema.png)

### Description ENG

The database represents restaurant serving orders to customers.

The restaurant has many menus to choose from, which include many dishes.

The restaurant stores data on its employees and customers. In addition, we record the addresses of each customer to
delivery orders to them. Employees, of course, have specific positions.

The key element of the database are orders, each of which has a corresponding type assigned to it. Multiple menu items
can be assigned to each order, also some of the same. In addition, to the order we assign the customer who ordered it.
Each customer can of course place multiple orders. We also assign employees who took part in the preparation or delivery
of the order. Employees in their work take part in the preparation of multiple orders.

### Description PL

Baza danych przedstawia restaurację serwującą zamówienia dla klientów.

Restauracja posiada wiele menu do wyboru, które zawiera wiele potraw.

W restauracji przechowywane są dane dotyczące pracowników oraz klientów. Dodatkowo zapisujemy adresy poszczególnych
klientów, aby umożliwić dostarczenie zamówień. Pracownicy również, oczywiście mają określone stanowiska.

Kluczowym elementem bazy danych są zamówienia, z których każde ma przypisany odpowiedni typ. Do każdego zamówienia można
przypisać wiele pozycji z menu, rowniez kilka takich samych. Dodatkowo do zamówienia przypisujemy klienta, który je
zamówił. Każdy klient może naturalne składać wiele zamówień. Do zamówienia przypisujemy również pracowników, którzy
brali udział w przygotowaniu lub dostarczeniu zamówienia. Pracownicy w trakcie swojej pracy biorą udział w przygotowaniu
wielu zamówień.

## Project Status

Project is: _in_progress_

## Room for Improvement

Room for improvement:

- Add frontend based on Web Components
- Add Rest API tests prepared in Rest Assured
- Add entities described in database
- Add SELECT queries, PL/SQL procedures and triggers
- Break monolith on microservices

## Contact

Created by https://github.com/KubaTworek
