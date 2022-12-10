CREATE TABLE IF NOT EXISTS `authorities` (
             `id` UUID NOT NULL UNIQUE PRIMARY KEY,
             `authority` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `users` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `username` VARCHAR(50) NOT NULL,
            `password` VARCHAR(255) NOT NULL,
            `role_id` UUID,
            FOREIGN KEY(`role_id`) REFERENCES `authorities`(`id`)
);

CREATE TABLE IF NOT EXISTS `job` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `type_of_order` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `type` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `menu` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `employee` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `first_name` VARCHAR(30) NOT NULL,
            `last_name` VARCHAR(30) NOT NULL,
            `job_id` UUID,
            FOREIGN KEY(`job_id`) REFERENCES `job`(`id`)
);

CREATE TABLE IF NOT EXISTS `orders` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `price` DECIMAL(8,2) NOT NULL DEFAULT 0,
            `date` DATE DEFAULT NULL,
            `hour_order` TIME DEFAULT NULL,
            `hour_away` TIME DEFAULT NULL,
            `type_of_order_id` UUID,
            `user_id` UUID,
            FOREIGN KEY(`type_of_order_id`) REFERENCES `type_of_order`(`id`),
            FOREIGN KEY(`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE IF NOT EXISTS `menu_item` (
            `id` UUID NOT NULL UNIQUE PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL,
            `price` DECIMAL(8,2) NOT NULL,
            `menu_id` UUID,
            FOREIGN KEY(`menu_id`) REFERENCES `menu`(`id`)
);

CREATE TABLE IF NOT EXISTS `order_employee` (
            `order_id` UUID NOT NULL,
            `employee_id` UUID NOT NULL,
            CONSTRAINT `pk_order_employee` PRIMARY KEY(`order_id`,`employee_id`),
            FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`),
            FOREIGN KEY(`employee_id`) REFERENCES `employee`(`id`)
);

CREATE TABLE IF NOT EXISTS `order_menu_item` (
            `order_id` UUID NOT NULL,
            `menu_item_id` UUID NOT NULL,
            CONSTRAINT `order_menu_item` PRIMARY KEY(`order_id`,`menu_item_id`),
            FOREIGN KEY(`order_id`) REFERENCES `orders`(`id`),
            FOREIGN KEY(`menu_item_id`) REFERENCES `menu_item`(`id`)
);

