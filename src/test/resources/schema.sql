CREATE TABLE IF NOT EXISTS `job` (
                       `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       `name` varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `type_of_order` (
                                 `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                 `type` varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `menu` (
                        `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        `name` varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `employee` (
                            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `first_name` varchar(30) NOT NULL,
                            `last_name` varchar(30) NOT NULL,
                            `job_id` INTEGER,
                            FOREIGN KEY(`job_id`) REFERENCES `job`(`id`)
);

CREATE TABLE IF NOT EXISTS `orders` (
                          `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `price` decimal NOT NULL DEFAULT 0,
                          `date` varchar(10) DEFAULT NULL,
                          `hour_order` varchar(5) DEFAULT NULL,
                          `hour_away` varchar(5) DEFAULT NULL,
                          `type_of_order_id` INTEGER,
                          FOREIGN KEY(`type_of_order_id`) REFERENCES `type_of_order`(`id`)
);

CREATE TABLE IF NOT EXISTS `menu_item` (
                             `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             `name` varchar(20) NOT NULL,
                             `price` decimal(8,2) NOT NULL,
                             `menu_id` INTEGER,
                             FOREIGN KEY(`menu_id`) REFERENCES `menu`(`id`)
);

CREATE TABLE IF NOT EXISTS `order_employee` (
                                  `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  `order_id` INTEGER NOT NULL,
                                  `employee_id` INTEGER NOT NULL,
                                  FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`),
                                  FOREIGN KEY(`employee_id`) REFERENCES `employee`(`id`)
);

CREATE TABLE IF NOT EXISTS `order_menu_item` (
                                   `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   `order_id` INTEGER NOT NULL,
                                   `menu_item_id` INTEGER NOT NULL,
                                   FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`),
                                   FOREIGN KEY(`menu_item_id`) REFERENCES `menu_item`(`id`)
);