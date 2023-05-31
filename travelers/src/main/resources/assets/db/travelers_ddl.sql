-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema travelers
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema travelers
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `travelers` DEFAULT CHARACTER SET utf8mb4 ;
USE `travelers` ;

-- -----------------------------------------------------
-- Table `travelers`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`user` (
  `idx` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(50) NOT NULL,
  `password` CHAR(64) NOT NULL,
  `password_salt` CHAR(32) NOT NULL,
  `name` VARCHAR(10) NOT NULL,
  `id` VARCHAR(20) NOT NULL,
  `token` VARCHAR(1000) NULL DEFAULT NULL,
  `birth` DATE NULL DEFAULT NULL,
  `gender` VARCHAR(10) NULL DEFAULT NULL,
  `phone` CHAR(11) NULL DEFAULT NULL,
  `register_date` DATE NOT NULL DEFAULT (current_date),
  `address` VARCHAR(30) NULL DEFAULT NULL,
  `grade` VARCHAR(15) NOT NULL DEFAULT 'bronze',
  `exp` INT NOT NULL DEFAULT '0',
  `last_signin_date` DATE NOT NULL DEFAULT (current_date),
  `profile_msg` VARCHAR(100) NULL DEFAULT NULL,
  `profile_img` BLOB NULL DEFAULT NULL,
  `devices` JSON NULL DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone_UNIQUE` (`phone` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`article` (
  `article_no` INT NOT NULL AUTO_INCREMENT,
  `article_type` VARCHAR(15) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `write_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `content` TEXT NOT NULL,
  `hit` INT NULL DEFAULT '0',
  `like` INT NULL DEFAULT '0',
  PRIMARY KEY (`article_no`),
  UNIQUE INDEX `article_no_UNIQUE` (`article_no` ASC) VISIBLE,
  INDEX `article_user_id_to_user_id_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `article_user_id_to_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `travelers`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`article_bookmark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`article_bookmark` (
  `email` VARCHAR(50) NOT NULL,
  `content_id` INT NULL DEFAULT '0',
  `article_no` INT NOT NULL,
  `article_type` VARCHAR(15) NOT NULL,
  `is_like` TINYINT(1) NULL DEFAULT '0',
  `is_bookmark` TINYINT(1) NULL DEFAULT '0',
  UNIQUE INDEX `email_and_article_no` (`email` ASC, `article_no` ASC) VISIBLE,
  INDEX `article_bookmark_email_to_user_email_fk_idx` (`email` ASC) VISIBLE,
  INDEX `article_bookmark_article_no_to_article_article_no_fk_idx` (`article_no` ASC) VISIBLE,
  CONSTRAINT `article_bookmark_article_no_to_article_article_no_fk_idx`
    FOREIGN KEY (`article_no`)
    REFERENCES `travelers`.`article` (`article_no`),
  CONSTRAINT `article_bookmark_email_to_user_email_fk_idx`
    FOREIGN KEY (`email`)
    REFERENCES `travelers`.`user` (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`sido` (
  `sido_code` INT NOT NULL,
  `sido_name` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `travelers`.`gugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`gugun` (
  `gugun_code` INT NOT NULL,
  `gugun_name` VARCHAR(30) NULL DEFAULT NULL,
  `sido_code` INT NOT NULL,
  PRIMARY KEY (`gugun_code`, `sido_code`),
  INDEX `gugun_to_sido_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  CONSTRAINT `gugun_to_sido_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `travelers`.`sido` (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `travelers`.`attraction_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`attraction_info` (
  `content_id` INT NOT NULL,
  `content_type_id` INT NULL DEFAULT NULL,
  `title` VARCHAR(100) NULL DEFAULT NULL,
  `addr1` VARCHAR(100) NULL DEFAULT NULL,
  `addr2` VARCHAR(50) NULL DEFAULT NULL,
  `zipcode` VARCHAR(50) NULL DEFAULT NULL,
  `tel` VARCHAR(50) NULL DEFAULT NULL,
  `first_image` VARCHAR(200) NULL DEFAULT NULL,
  `first_image2` VARCHAR(200) NULL DEFAULT NULL,
  `readcount` INT NULL DEFAULT NULL,
  `sido_code` INT NULL DEFAULT NULL,
  `gugun_code` INT NULL DEFAULT NULL,
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `mlevel` VARCHAR(2) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `attraction_to_content_type_id_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attraction_to_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `attraction_to_gugun_code_fk_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `attraction_to_content_type_id_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `travelers`.`content_type` (`content_type_id`),
  CONSTRAINT `attraction_to_gugun_code_fk`
    FOREIGN KEY (`gugun_code`)
    REFERENCES `travelers`.`gugun` (`gugun_code`),
  CONSTRAINT `attraction_to_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `travelers`.`sido` (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `travelers`.`attraction_bookmark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`attraction_bookmark` (
  `idx` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(50) NOT NULL,
  `content_id` INT NOT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC, `content_id` ASC) VISIBLE,
  INDEX `attr_bookmark_email_to_user_email_fk_idx` (`email` ASC) VISIBLE,
  INDEX `attr_bookmark_content_id_to_attraction_info_content_id_fk_idx` (`content_id` ASC) VISIBLE,
  CONSTRAINT `attr_bookmark_content_id_to_attraction_info_content_id_fk_idx`
    FOREIGN KEY (`content_id`)
    REFERENCES `travelers`.`attraction_info` (`content_id`),
  CONSTRAINT `attr_bookmark_email_to_user_email_fk_idx`
    FOREIGN KEY (`email`)
    REFERENCES `travelers`.`user` (`email`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`attraction_description`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`attraction_description` (
  `content_id` INT NOT NULL,
  `homepage` VARCHAR(100) NULL DEFAULT NULL,
  `overview` VARCHAR(10000) NULL DEFAULT NULL,
  `telname` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_attraciton_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `travelers`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `travelers`.`attraction_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`attraction_detail` (
  `content_id` INT NOT NULL,
  `cat1` VARCHAR(3) NULL DEFAULT NULL,
  `cat2` VARCHAR(5) NULL DEFAULT NULL,
  `cat3` VARCHAR(9) NULL DEFAULT NULL,
  `created_time` VARCHAR(14) NULL DEFAULT NULL,
  `modified_time` VARCHAR(14) NULL DEFAULT NULL,
  `booktour` VARCHAR(5) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_basic_content_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `travelers`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `travelers`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`comment` (
  `article_no` INT NOT NULL,
  `comment_no` INT NOT NULL AUTO_INCREMENT,
  `parent_comment_no` INT NULL DEFAULT '0',
  `user_id` VARCHAR(20) NOT NULL,
  `write_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `content` VARCHAR(144) NOT NULL,
  `like` INT NULL DEFAULT '0',
  `is_deleted` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`comment_no`),
  UNIQUE INDEX `comment_no_UNIQUE` (`comment_no` ASC) VISIBLE,
  INDEX `comment_user_id_to_user_id_fk_idx` (`user_id` ASC) VISIBLE,
  INDEX `comment_article_no_to_article_article_no_fk_idx` (`article_no` ASC) VISIBLE,
  CONSTRAINT `comment_article_no_to_article_article_no_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `travelers`.`article` (`article_no`),
  CONSTRAINT `comment_user_id_to_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `travelers`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`file_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`file_info` (
  `idx` INT NOT NULL AUTO_INCREMENT,
  `id` VARCHAR(20) NULL DEFAULT NULL,
  `article_no` INT NULL DEFAULT NULL,
  `save_folder` VARCHAR(45) NULL DEFAULT NULL,
  `original_file` VARCHAR(50) NULL DEFAULT NULL,
  `save_file` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`idx`),
  INDEX `file_info_to_article_article_no_fk` (`article_no` ASC) VISIBLE,
  INDEX `file_info_to_user_id_fk` (`id` ASC) VISIBLE,
  CONSTRAINT `file_info_to_article_article_no_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `travelers`.`article` (`article_no`),
  CONSTRAINT `file_info_to_user_id_fk`
    FOREIGN KEY (`id`)
    REFERENCES `travelers`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`hotplace`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`hotplace` (
  `article_no` INT NOT NULL,
  `content_id` INT NOT NULL,
  `content_type_id` INT NULL DEFAULT '0',
  `rate` INT NOT NULL,
  `visit_date` DATE NOT NULL,
  `image` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`article_no`),
  UNIQUE INDEX `article_no_UNIQUE` (`article_no` ASC) VISIBLE,
  CONSTRAINT `FK_article_TO_hotplace_1`
    FOREIGN KEY (`article_no`)
    REFERENCES `travelers`.`article` (`article_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `travelers`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travelers`.`schedule` (
  `article_no` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `path` JSON NOT NULL,
  PRIMARY KEY (`article_no`),
  UNIQUE INDEX `article_no_UNIQUE` (`article_no` ASC) VISIBLE,
  CONSTRAINT `FK_article_TO_schedule_1`
    FOREIGN KEY (`article_no`)
    REFERENCES `travelers`.`article` (`article_no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
