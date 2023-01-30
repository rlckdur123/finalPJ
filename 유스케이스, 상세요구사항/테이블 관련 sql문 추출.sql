-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema social2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema social2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `social2` DEFAULT CHARACTER SET utf8 ;
USE `social2` ;

-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_USER` (
  `USER_ID` INT NOT NULL,
  `USER_NAME` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NOT NULL,
  `FIRST_NAME` VARCHAR(45) NOT NULL,
  `LAST_NAME` VARCHAR(45) NOT NULL,
  `PHONE` VARCHAR(45) NULL,
  `EMAIL` VARCHAR(45) NULL,
  `ADDRESS` VARCHAR(100) NULL,
  `BIO` VARCHAR(300) NULL,
  `REGDATE` DATETIME NOT NULL,
  `USER_ROLE` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`USER_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_USER_RELATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_USER_RELATION` (
  `USER_ID` INT NOT NULL,
  `FOLLOWER_ID` INT NOT NULL,
  `FOLLOW_DATE` DATETIME NOT NULL,
  PRIMARY KEY (`USER_ID`),
  CONSTRAINT `fk_T_USER_RELATION_T_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `social2`.`T_MGLG_USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_POST`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_POST` (
  `POST_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  `POST_CONTENT` VARCHAR(1000) NOT NULL,
  `REST_NM` VARCHAR(45) NOT NULL,
  `POST_RATING` INT NOT NULL,
  `VIEW_COUNT` INT NULL,
  `POST_DATE` INT NOT NULL,
  PRIMARY KEY (`POST_ID`),
  INDEX `fk_T_POST_T_USER1_idx` (`USER_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_POST_T_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `social2`.`T_MGLG_USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_COMMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_COMMENT` (
  `COMMENT_ID` INT NOT NULL,
  `POST_ID` INT NOT NULL,
  `COMMENT_CONTENT` VARCHAR(100) NOT NULL,
  `COMMENT_DATE` DATETIME NOT NULL,
  `USER_ID` INT NOT NULL,
  PRIMARY KEY (`COMMENT_ID`, `POST_ID`),
  INDEX `fk_T_COMMENT_T_POST1_idx` (`POST_ID` ASC) VISIBLE,
  INDEX `fk_T_COMMENT_T_USER1_idx` (`USER_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_COMMENT_T_POST1`
    FOREIGN KEY (`POST_ID`)
    REFERENCES `social2`.`T_MGLG_POST` (`POST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_T_COMMENT_T_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `social2`.`T_MGLG_USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_BOARD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_BOARD` (
  `BOARD_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  `BOARD_TITLE` VARCHAR(45) NOT NULL,
  `BOARD_CONTENT` VARCHAR(1000) NOT NULL,
  `BOARD_COUNT` VARCHAR(45) NULL,
  `BOARD_DATE` DATETIME NOT NULL,
  PRIMARY KEY (`BOARD_ID`),
  INDEX `fk_T_BOARD_T_USER1_idx` (`USER_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_BOARD_T_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `social2`.`T_MGLG_USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_REPORT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_REPORT` (
  `REPORT_ID` INT NOT NULL,
  `REPORT_TYPE` INT NOT NULL,
  `SOURCE_USER_ID` INT NOT NULL,
  `TARGET_USER_ID` INT NOT NULL,
  `REPORT_DATE` DATETIME NOT NULL,
  `USER_ID` INT NULL,
  `POST_ID` INT NULL,
  `COMMENT_ID` INT NULL,
  PRIMARY KEY (`REPORT_ID`, `REPORT_TYPE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_POST_FILE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_POST_FILE` (
  `POST_ID` INT NOT NULL,
  `POST_FILE_ID` INT NOT NULL,
  `POST_FILE_NM` VARCHAR(150) NULL,
  `POST_FILE_ORIGIN_NM` VARCHAR(150) NULL,
  `POST_FILE_PATH` VARCHAR(150) NULL,
  `POST_FILE_REGDATE` DATETIME NULL,
  PRIMARY KEY (`POST_ID`, `POST_FILE_ID`),
  INDEX `fk_T_POST_FILE_T_POST1_idx` (`POST_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_POST_FILE_T_POST1`
    FOREIGN KEY (`POST_ID`)
    REFERENCES `social2`.`T_MGLG_POST` (`POST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_CHATROOM`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_CHATROOM` (
  `CHATROOM_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  `RECEIVER_ID` INT NOT NULL,
  `ROOM_DATE` DATETIME NOT NULL,
  `ROOM_USE_YN` CHAR(1) NULL,
  PRIMARY KEY (`CHATROOM_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_MESSAGE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_MESSAGE` (
  `MESSAGE_NO` INT NOT NULL,
  `CHATROOM_ID` INT NOT NULL,
  `USER_ID` INT NOT NULL,
  `RECEIVER_ID` INT NOT NULL,
  `MESSAGE_CONTENT` VARCHAR(300) NOT NULL,
  `SEND_TIME` DATETIME NOT NULL,
  `MESSAGE_READ_YN` CHAR(1) NULL,
  PRIMARY KEY (`MESSAGE_NO`, `CHATROOM_ID`),
  INDEX `fk_T_MGLG_MESSAGE_T_MGLG_CHATROOM1_idx` (`CHATROOM_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_MGLG_MESSAGE_T_MGLG_CHATROOM1`
    FOREIGN KEY (`CHATROOM_ID`)
    REFERENCES `social2`.`T_MGLG_CHATROOM` (`CHATROOM_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `social2`.`T_MGLG_LIKES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `social2`.`T_MGLG_LIKES` (
  `USER_ID` INT NOT NULL,
  `POST_ID` INT NOT NULL,
  `LIKE_DATE` DATETIME NOT NULL,
  PRIMARY KEY (`USER_ID`, `POST_ID`),
  INDEX `fk_T_MGLG_LIKES_T_MGLG_USER1_idx` (`USER_ID` ASC) VISIBLE,
  INDEX `fk_T_MGLG_LIKES_T_MGLG_POST1_idx` (`POST_ID` ASC) VISIBLE,
  CONSTRAINT `fk_T_MGLG_LIKES_T_MGLG_USER1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `social2`.`T_MGLG_USER` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_T_MGLG_LIKES_T_MGLG_POST1`
    FOREIGN KEY (`POST_ID`)
    REFERENCES `social2`.`T_MGLG_POST` (`POST_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
