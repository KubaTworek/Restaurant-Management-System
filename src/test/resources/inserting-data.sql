INSERT INTO `job`(`id`, `name`) VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan');
INSERT INTO `type_of_order`(`id`, `type`) VALUES (1, 'On-site'), (2, 'Delivery');
INSERT INTO `menu`(`id`, `name`) VALUES (1, 'Drinks'), (2, 'Food');
INSERT INTO `employee`(`id`, `first_name`, `last_name`, `job_id`) VALUES (1, 'John', 'Smith', 1), (2, 'James', 'Patel', 2), (3, 'Ann', 'Mary', 3);
INSERT INTO `orders`(`id`, `price`, `date`, `hour_order`, `hour_away`, `type_of_order_id`) VALUES (1, 12.99, '2022-08-22', '12:00', '12:15', 1), (2, 30.99, '2022-08-22', '12:05', null, 2);
INSERT INTO `menu_item`(`id`, `name`, price, `menu_id`) VALUES (1, 'Chicken', 10.99, 2), (2, 'Coke', 1.99, 1), (3, 'Tiramisu', 5.99, 2);
INSERT INTO `order_employee`(`order_id`, `employee_id`) VALUES (1, 1), (2, 1);
INSERT INTO `order_menu_item`(`order_id`, `menu_item_id`) VALUES (1, 1), (1, 2), (2, 3), (2, 2);
