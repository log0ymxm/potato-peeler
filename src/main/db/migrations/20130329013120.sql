START TRANSACTION;

CREATE TABLE IF NOT EXISTS `states` (
  `id` char(36) NOT NULL,
  `abbreviation` varchar(2) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ABBREV_NAME_STATES_UNIQUE` (`abbreviation`,`name`),
  UNIQUE KEY `ABBREV_STATES_UNIQUE` (`abbreviation`),
  UNIQUE KEY `NAME_STATES_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `states_GUID`;
DELIMITER //
CREATE TRIGGER `states_GUID` BEFORE INSERT ON `states`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `locations` (
  `id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `state_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_LOCATIONS_UNIQUE` (`name`),
  CONSTRAINT `FK_LOCATIONS_STATE_ID`
    FOREIGN KEY (`state_id`)
    REFERENCES `states` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `locations_GUID`;
DELIMITER //
CREATE TRIGGER `locations_GUID` BEFORE INSERT ON `locations`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `schools` (
  `id` char(36) NOT NULL,
  `rmp_id` int(11) NOT NULL,
  `location_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `RMP_ID_SCHOOLS_UNIQUE` (`rmp_id`),
  CONSTRAINT `FK_SCHOOLS_LOCATION_ID`
    FOREIGN KEY (`location_id`)
    REFERENCES `locations` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `schools_GUID`;
DELIMITER //
CREATE TRIGGER `schools_GUID` BEFORE INSERT ON `schools`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `departments` (
  `id` char(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_DEPARTMENTS_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `departments_GUID`;
DELIMITER //
CREATE TRIGGER `departments_GUID` BEFORE INSERT ON `departments`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `teachers` (
  `id` char(36) NOT NULL,
  `rmp_id` int(11) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `department_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `RMP_ID_TEACHERS_UNIQUE` (`rmp_id`),
  CONSTRAINT `FK_TEACHERS_DEPARTMENT_ID`
    FOREIGN KEY (`department_id`)
    REFERENCES `departments` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `teachers_GUID`;
DELIMITER //
CREATE TRIGGER `teachers_GUID` BEFORE INSERT ON `teachers`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `teachers_schools` (
  `teacher_id` char(36) NOT NULL,
  `school_id` char(36) NOT NULL,
  CONSTRAINT `FK_TEACHERS_SCHOOLS_TEACHER_ID`
    FOREIGN KEY (`teacher_id`)
    REFERENCES `teachers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_TEACHERS_SCHOOLS_SCHOOL_ID`
    FOREIGN KEY (`school_id`)
    REFERENCES `schools` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `school_ratings` (
  `id` char(36) NOT NULL,
  `rmp_id` int(11) NOT NULL,
  `school_id` char(36) NOT NULL,
  `date` date NOT NULL,
  `school_reputation` float NOT NULL,
  `career_opportunities` float NOT NULL,
  `campus_grounds` float NOT NULL,
  `quality_of_food` float NOT NULL,
  `social_activities` float NOT NULL,
  `campus_location` float NOT NULL,
  `condition_of_library` float NOT NULL,
  `internet_speed` float NOT NULL,
  `clubs_and_events` float NOT NULL,
  `comment` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `RMP_ID_SCHOOL_RATINGS_UNIQUE` (`rmp_id`),
  CONSTRAINT `FK_SCHOOL_RATINGS_SCHOOL_ID`
    FOREIGN KEY (`school_id`)   
    REFERENCES `schools` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FULLTEXT (`comment`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `school_ratings_GUID`;
DELIMITER //
CREATE TRIGGER `school_ratings_GUID` BEFORE INSERT ON `school_ratings`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;


CREATE TABLE IF NOT EXISTS `teacher_ratings` (
  `id` char(36) NOT NULL,
  `rmp_id` int(11) NOT NULL,
  `easiness` float NOT NULL,
  `helpfulness` float NOT NULL,
  `clarity` float NOT NULL,
  `rater_interest` float NOT NULL,
  `comment` text NOT NULL,
  `class_id` char(36) NOT NULL,
  `date` date NOT NULL,
  `teacher_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `RMP_ID_TEACHER_RATINGS_UNIQUE` (`rmp_id`),
  CONSTRAINT `FK_TEACHER_RATINGS_CLASS_ID`
    FOREIGN KEY (`class_id`)   
    REFERENCES `classes` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_TEACHER_RATINGS_TEACHER_ID`
    FOREIGN KEY (`teacher_id`)   
    REFERENCES `teachers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FULLTEXT (`comment`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `teacher_ratings_GUID`;
DELIMITER //
CREATE TRIGGER `teacher_ratings_GUID` BEFORE INSERT ON `teacher_ratings`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;


CREATE TABLE IF NOT EXISTS `teacher_feedback` (
  `id` char(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `teacher_feedback_GUID`;
DELIMITER //
CREATE TRIGGER `teacher_feedback_GUID` BEFORE INSERT ON `teacher_feedback`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;


CREATE TABLE IF NOT EXISTS `classes` (
  `id` char(36) NOT NULL,
  `department_id` char(36) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_CLASSES_DEPARTMENT_ID`
    FOREIGN KEY (`department_id`)   
    REFERENCES `departments` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `classes_GUID`;
DELIMITER //
CREATE TRIGGER `classes_GUID` BEFORE INSERT ON `classes`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;


CREATE TABLE IF NOT EXISTS `transcripts` (
  `id` char(36) NOT NULL,
  `is_predicted` boolean NOT NULL DEFAULT false,
  `date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `transcripts_GUID`;
DELIMITER //
CREATE TRIGGER `transcripts_GUID` BEFORE INSERT ON `transcripts`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;


CREATE TABLE IF NOT EXISTS `transcript_records` (
  `id` char(36) NOT NULL,
  `transcript_id` char(36) NOT NULL,
  `grade` float NOT NULL,
  `teacher_id` char(36) NOT NULL,
  `class_id` char(36) NOT NULL,
  `comment` text,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TRANSCRIPT_RECORDS_TRANSCRIPT_ID`
    FOREIGN KEY (`transcript_id`)   
    REFERENCES `transcripts` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_TRANSCRIPT_RECORDS_TEACHER_ID`
    FOREIGN KEY (`teacher_id`)   
    REFERENCES `teachers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_TRANSCRIPT_RECORDS_CLASS_ID`
    FOREIGN KEY (`class_id`)   
    REFERENCES `classs` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FULLTEXT (`comment`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TRIGGER IF EXISTS `transcript_records_GUID`;
DELIMITER //
CREATE TRIGGER `transcript_records_GUID` BEFORE INSERT ON `transcript_records`
 FOR EACH ROW begin
 SET new.id = UUID();
end//
DELIMITER ;



COMMIT;
