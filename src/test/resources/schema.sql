CREATE TABLE IF NOT EXISTS `job` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `type_of_order` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `type` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `menu` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `employee` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `first_name` VARCHAR(30) NOT NULL,
            `last_name` VARCHAR(30) NOT NULL,
            `job_id` INTEGER,
            FOREIGN KEY(`job_id`) REFERENCES `job`(`id`)
);

CREATE TABLE IF NOT EXISTS `orders` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `price` DECIMAL(8,2) NOT NULL DEFAULT 0,
            `date` DATE DEFAULT NULL,
            `hour_order` TIME DEFAULT NULL,
            `hour_away` TIME DEFAULT NULL,
            `type_of_order_id` INTEGER,
            FOREIGN KEY(`type_of_order_id`) REFERENCES `type_of_order`(`id`)
);

CREATE TABLE IF NOT EXISTS `menu_item` (
            `id` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `name` VARCHAR(20) NOT NULL,
            `price` DECIMAL(8,2) NOT NULL,
            `menu_id` INTEGER,
            FOREIGN KEY(`menu_id`) REFERENCES `menu`(`id`)
);

CREATE TABLE IF NOT EXISTS `order_employee` (
            `order_id` INTEGER NOT NULL,
            `employee_id` INTEGER NOT NULL,
            CONSTRAINT `pk_order_employee` PRIMARY KEY(`order_id`,`employee_id`),
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