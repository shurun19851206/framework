-- 需要注意的是，在建表中必须使用users做为表名，并且表中必须含有username和password这两个字段，其他的字段可以自行扩展
-- 通常为了以后扩展使用，在建立users表时，建议将所有的shiro.ini中所有可以使用配置的都在该表中预留一个字段，方便以后使用
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
