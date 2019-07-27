CREATE TABLE `d_demo`.`t_users`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` int(3) NULL DEFAULT 0,
  `add_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
INSERT INTO `d_demo`.`t_users`(`id`, `login_name`, `password`, `age`, `add_time`) VALUES (1, '老孙', '111111', 11, '2018-09-25 15:06:45');
INSERT INTO `d_demo`.`t_users`(`id`, `login_name`, `password`, `age`, `add_time`) VALUES (2, '孙哥', '222222', 122, '2018-09-25 15:14:21');