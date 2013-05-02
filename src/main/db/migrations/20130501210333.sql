ALTER TABLE `transcript_records` ADD `school_id` char(36);
ALTER TABLE `transcript_records` ADD `state_id` char(36);
ALTER TABLE `transcript_records` MODIFY `grade` float NULL;
ALTER TABLE `transcript_records` MODIFY `teacher_id` char(36) NULL;

ALTER TABLE `transcript_records` ADD INDEX `TRANSCRIPT_RECORDS_SCHOOL_ID_INDEX` (`school_id`);
ALTER TABLE `transcript_records` ADD INDEX `TRANSCRIPT_RECORDS_STATE_ID_INDEX` (`state_id`);
