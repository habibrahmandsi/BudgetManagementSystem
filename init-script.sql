
/* init script */

INSERT INTO `user` (`id`, `email`, `enabled`, `first_name`, `last_name`, `password`, `role`, `username`)
VALUES (NULL, 'sumon050789@gmail.com', b'1', 'Md. habibur ', 'Rahaman', '12345678', 'ROLE_ADMIN', 'habib');

INSERT INTO `expense_item` (`id`, `description`, `name`) VALUES (NULL, 'Cement', 'Buy Cement');
INSERT INTO `expense_item` (`id`, `description`, `name`) VALUES (NULL, 'Et', 'Buy Et');
INSERT INTO `expense_item` (`id`, `description`, `name`) VALUES (NULL, 'Soil test', 'Soil test');
INSERT INTO `expense_item` (`id`, `description`, `name`) VALUES (NULL, 'Plan pass', 'Plan pass');