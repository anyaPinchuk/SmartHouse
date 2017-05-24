CREATE DATABASE IF NOT EXISTS `usersdb` DEFAULT CHARACTER SET utf8 ;
USE `usersdb` ;

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
  `name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL,
  `id_house` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `UK_587tdsv8u5cvheyo9i261xhry` (`login` ASC),
  INDEX `FK_4py7q73xa95tp39ivq8oihyj6` (`id_house` ASC),
  CONSTRAINT `FK_4py7q73xa95tp39ivq8oihyj6`
  FOREIGN KEY (`id_house`)
  REFERENCES `usersdb`.`house` (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `usersdb`.`restriction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`restriction` (
  `id_restriction` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `end_time` VARCHAR(255) NULL DEFAULT NULL,
  `hours` VARCHAR(255) NULL DEFAULT NULL,
  `secured` BIT(1) NOT NULL,
  `start_time` VARCHAR(255) NULL DEFAULT NULL,
  `id_device` BIGINT(20) NULL DEFAULT NULL,
  `id_user` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id_restriction`),
  INDEX `FK_nxd9t39ng0uchpdkrnw6h2fyc` (`id_device` ASC),
  INDEX `FK_ba8dp7qb0qjwoic9803gxue7m` (`id_user` ASC),
  CONSTRAINT `FK_ba8dp7qb0qjwoic9803gxue7m`
  FOREIGN KEY (`id_user`)
  REFERENCES `usersdb`.`user` (`id_user`),
  CONSTRAINT `FK_nxd9t39ng0uchpdkrnw6h2fyc`
  FOREIGN KEY (`id_device`)
  REFERENCES `usersdb`.`device` (`id_device`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `usersdb`.`user_email`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`user_email` (
  `id_user_email` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `expire_date` DATETIME NOT NULL,
  `unique_key` VARCHAR(255) NOT NULL,
  `id_house` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id_user_email`),
  UNIQUE INDEX `UK_qm35y897bny0qjqutviap50yi` (`email` ASC),
  UNIQUE INDEX `UK_7f4uhpa8yil3xnh9paq3210r5` (`unique_key` ASC),
  INDEX `FK_8ssmhb4p88t7ghsa2sr384wv7` (`id_house` ASC),
  CONSTRAINT `FK_8ssmhb4p88t7ghsa2sr384wv7`
  FOREIGN KEY (`id_house`)
  REFERENCES `usersdb`.`house` (`id_house`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


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

INSERT INTO `usersdb`.`user` (`date_of_registration`, `login`, `name`, `password`, `role`)
VALUES ('2017-05-05', 'anyapinchuk3@gmail.com', 'Ann', 'b59c67bf196a4758191e42f76670ceba',
        'ROLE_ADMIN');
