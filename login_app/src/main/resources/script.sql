CREATE TABLE IF NOT EXISTS `usersdb`.`device` (
  `id_device` INT(4) UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `model` VARCHAR(45) NULL,
  `power` VARCHAR(5) NOT NULL,
  `power_unit` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id_device`),
  UNIQUE INDEX `id_device_UNIQUE` (`id_device` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `usersdb`.`work_log` (
  `id_work_log` INT(10) NOT NULL,
  `consumed_energy` VARCHAR(5) NULL,
  `date_of_action` TIMESTAMP(14) NOT NULL,
  `device_state` VARCHAR(5) NOT NULL,
  `id_device` INT(4) UNSIGNED NOT NULL,
  PRIMARY KEY (`id_work_log`, `id_device`),
  INDEX `fk_work_log_device_idx` (`id_device` ASC),
  CONSTRAINT `fk_work_log_device`
    FOREIGN KEY (`id_device`)
    REFERENCES `usersdb`.`device` (`id_device`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;