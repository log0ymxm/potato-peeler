CREATE TABLE IF NOT EXISTS `web_scrapes` (
  `id` char(36) NOT NULL DEFAULT 0,
  `task` varchar(255) NOT NULL,
  `started` datetime,
  `finished` datetime,
  `records` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `web_scrapes_GUID`;
DELIMITER //
CREATE TRIGGER `web_scrapes_GUID` BEFORE INSERT ON `web_scrapes`
 FOR EACH ROW begin
 SET NEW.id = UUID(), NEW.started = NOW();
end//
DELIMITER ;
