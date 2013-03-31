ALTER TABLE `teachers` ADD COLUMN `school_id` CHAR(36);

ALTER TABLE `teachers` ADD CONSTRAINT `FK_TEACHERS_SCHOOL_ID` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`);
