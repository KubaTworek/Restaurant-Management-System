# Restaurant Management System
> REST APIs based on CRUD methods. The project was written in Java using Spring. The database tries to maintain a third normal form, it was built on an internal H2 database. The Admin position gives us the ability to manage the restaurant, modify the menu and employees, and collect information on placed orders. User privileges allow us to place orders, while we don't even need to be authenticated to view the menu. The project has been written in accordance with the principles of Clean Code and good object-oriented programming practices. All classes and methods have been tested with unit and integration tests.


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
- Restaurant manager gives us the ability to add, modify and delete menus and the dishes belonging to them. We also have the ability to manage employees such as cooks, waiters and delivery men. The customer has the ability to place orders on the spot and for take-out. The restaurant also has a cook and order delivery system, which is used by waiters and delivery personnel. A system of authentication and authorization by login data and JWT token has been introduced.


- The purpose of writing this project is to improve my programming skills, learn various tools and demonstrate my ability to write readable and working code.


- I prepared a project with similar requirements in my GUI classes (https://github.com/KubaTworek/Restaurant-Management) but I decided to develop it further as a full-fledged web application using a wide range of tools.


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
![Example screenshot](database-schema.png)
<!-- If you have screenshots you'd like to share, include them here. -->

## Project Status
Project is: _in_progress_


## Room for Improvement

Room for improvement:
- Add frontend build in React
- Add Rest API tests prepared in Rest Assured
- Add restaurants and owners of them
- Add couriers

## Contact
Created by me :)
