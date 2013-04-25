CREATE TABLE IF NOT EXISTS `userinfo` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
);
  
INSERT INTO `users` (`id`,`username`,`password`) VALUES (1,'admin','password');