CREATE DATABASE IF NOT EXISTS `usersdb` DEFAULT CHARACTER SET utf8 ;
USE `usersdb` ;

-- -----------------------------------------------------
-- Table `usersdb`.`house`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`house` (
  `id_house` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `owner_login` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `usersdb`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`address` (
  `id_address` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `country` VARCHAR(255) NULL DEFAULT NULL,
  `flat` VARCHAR(255) NULL DEFAULT NULL,
  `house` VARCHAR(255) NULL DEFAULT NULL,
  `street` VARCHAR(255) NULL DEFAULT NULL,
  `id_house` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id_address`),
  INDEX `FK_obmd2b1h2ubtcrx5v95is3hi7` (`id_house` ASC),
  CONSTRAINT `FK_obmd2b1h2ubtcrx5v95is3hi7`
  FOREIGN KEY (`id_house`)
  REFERENCES `usersdb`.`house` (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `usersdb`.`device`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`device` (
  `id_device` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `model` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `power` VARCHAR(255) NOT NULL,
  `state` VARCHAR(255) NULL DEFAULT NULL,
  `id_house` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id_device`),
  INDEX `FK_gs72135au42vaoctwlu6vk72r` (`id_house` ASC),
  CONSTRAINT `FK_gs72135au42vaoctwlu6vk72r`
  FOREIGN KEY (`id_house`)
  REFERENCES `usersdb`.`house` (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `usersdb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`user` (
  `id_user` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `date_of_registration` DATE NOT NULL,
  `login` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL,
  `id_house` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  INDEX `FK_4py7q73xa95tp39ivq8oihyj6` (`id_house` ASC),
  CONSTRAINT `FK_4py7q73xa95tp39ivq8oihyj6`
  FOREIGN KEY (`id_house`)
  REFERENCES `usersdb`.`house` (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `usersdb`.`user_email`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`user_email` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `encoded_email` VARCHAR(255) UNIQUE NOT NULL,
  `expire_date` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_qm35y897bny0qjqutviap50yi` (`email` ASC))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`restriction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`restriction` (
  `id_restriction` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `start_time` VARCHAR(45) NULL,
  `end_time` VARCHAR(45) NULL,
  `hours` VARCHAR(5) NULL,
  `id_device` BIGINT(20) NOT NULL,
  `id_user` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_restriction`),
  UNIQUE INDEX `id_UNIQUE` (`id_restriction` ASC),
  INDEX `fk_restriction_device_idx` (`id_device` ASC),
  INDEX `fk_restriction_user1_idx` (`id_user` ASC),
  CONSTRAINT `fk_restriction_device`
  FOREIGN KEY (`id_device`)
  REFERENCES `usersdb`.`device` (`id_device`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_restriction_user1`
  FOREIGN KEY (`id_user`)
  REFERENCES `usersdb`.`user` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usersdb`.`work_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`work_log` (
  `id_work_log` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `action` VARCHAR(255) NOT NULL,
  `consumed_energy` VARCHAR(255) NULL DEFAULT NULL,
  `date_of_action` DATETIME NOT NULL,
  `id_device` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_work_log`),
  INDEX `FK_oy0q37x9lcl0xh8xk95sm9hwx` (`id_device` ASC),
  CONSTRAINT `FK_oy0q37x9lcl0xh8xk95sm9hwx`
  FOREIGN KEY (`id_device`)
  REFERENCES `usersdb`.`device` (`id_device`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

INSERT INTO `usersdb`.`user` (`date_of_registration`, `login`, `password`, `role`)
VALUES ('2017-05-05', 'anyapinchuk3@gmail.com', 'b59c67bf196a4758191e42f76670ceba',
        'ROLE_ADMIN');
