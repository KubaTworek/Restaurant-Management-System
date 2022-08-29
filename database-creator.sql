CREATE DATABASE  IF NOT EXISTS `restaurantmanager_db`;
USE `restaurantmanager_db`;

CREATE TABLE `Job` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(20) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- INSERT INTO `Job` VALUES
-- (1,'Cook'),
-- (2,'Waiter'),
-- (3,'DeliveryMan');

CREATE TABLE `Type_Of_Order` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `type` varchar(20) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- INSERT INTO `TypeOfOrder` VALUES
-- (1,'On-site'),
-- (2,'Delivery');

CREATE TABLE `Menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(20) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `job_id` int(11),
  FOREIGN KEY(`job_id`) REFERENCES `Job`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `price` decimal(8,2) NOT NULL DEFAULT 0,
  `date` date DEFAULT NULL,
  `hour_order` time DEFAULT NULL,
  `hour_away` time DEFAULT NULL,
  `type_of_order_id` int(11),
  FOREIGN KEY(`type_of_order_id`) REFERENCES `Type_Of_Order`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Menu_Item` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(20) NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `menu_id` int(11),
  FOREIGN KEY(`menu_id`) REFERENCES `Menu`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Order_Employee` (
`id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `order_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  FOREIGN KEY(`order_id`) REFERENCES `Orders`(`id`),
  FOREIGN KEY(`employee_id`) REFERENCES `Employee`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Order_Menu_Item` (
  `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `order_id` int(11) NOT NULL,
  `menu_item_id` int(11) NOT NULL,
  FOREIGN KEY(`order_id`) REFERENCES `Orders`(`id`),
  FOREIGN KEY(`menu_item_id`) REFERENCES `Menu_Item`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `Job` VALUES 
(1,'Cook'),
(2,'Waiter'),
(3,'DeliveryMan');

INSERT INTO `Employee` VALUES 
(1,'John','Smith',1),
(2,'James','Patel',2),
(3,'Ann','Mary',3);

INSERT INTO `Type_Of_Order` VALUES
(1,'On-site'),
(2,'Delivery');

INSERT INTO `Menu` VALUES
(1,'Drinks'),
(2,'Food');

INSERT INTO `Orders` VALUES
(1,12.99,'2022-08-22','12:00','12:15',1),
(2,30.99,'2022-08-22','12:05','12:15',2);

INSERT INTO `Menu_Item` VALUES 
(1,'Chicken',10.99,2),
(2,'Coke',1.99,1),
(3,'Tiramisu',5.99,2);

INSERT INTO `Order_Employee` VALUES 
(1,1,1),
(2,2,1);

INSERT INTO `Order_Menu_Item` VALUES 
(1,1,1),
(2,1,2),
(3,2,3),
(4,2,2);


