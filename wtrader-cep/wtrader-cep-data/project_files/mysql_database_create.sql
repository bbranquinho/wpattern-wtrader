SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `market_data` ;
CREATE SCHEMA IF NOT EXISTS `market_data` DEFAULT CHARACTER SET utf8 ;
USE `market_data` ;

-- -----------------------------------------------------
-- Table `market_data`.`tb_benefit_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_benefit_type` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_benefit_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL COMMENT '0 = Bonificacao\n1 = Desdobramento\n2 = Grupamento',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `market_data`.`tb_company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_company` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_company` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code_cvm` VARCHAR(20) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `unitary_quotation_since` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `code_cvm_UNIQUE` (`code_cvm` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 449;


-- -----------------------------------------------------
-- Table `market_data`.`tb_benefit_stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_benefit_stock` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_benefit_stock` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `deliberate` DATE NOT NULL,
  `businesses_with_up` DATE NOT NULL,
  `shares_in_credit` DATE NULL DEFAULT NULL,
  `factor` DOUBLE NULL,
  `company_id` INT(11) NOT NULL,
  `benefit_type_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tb_benefit_stock_tb_company1_idx` (`company_id` ASC),
  INDEX `fk_tb_benefit_stock_tb_benefit_type1_idx` (`benefit_type_id` ASC),
  CONSTRAINT `fk_tb_benefit_stock_tb_benefit_type1`
    FOREIGN KEY (`benefit_type_id`)
    REFERENCES `market_data`.`tb_benefit_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_benefit_stock_tb_company1`
    FOREIGN KEY (`company_id`)
    REFERENCES `market_data`.`tb_company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `market_data`.`tb_stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_stock` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_stock` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  `was_normalized` TINYINT(1) NOT NULL DEFAULT false,
  `empty_records_percentual` DOUBLE NULL,
  `company_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  INDEX `fk_tb_stock_tb_company1_idx` (`company_id` ASC),
  CONSTRAINT `fk_tb_stock_tb_company1`
    FOREIGN KEY (`company_id`)
    REFERENCES `market_data`.`tb_company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 801;


-- -----------------------------------------------------
-- Table `market_data`.`tb_stock_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_stock_record` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_stock_record` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `start_price` DOUBLE NOT NULL,
  `highest_negotiation_price` DOUBLE NOT NULL,
  `lowest_negotiation_price` DOUBLE NOT NULL,
  `average_negotiation_price` DOUBLE NOT NULL,
  `closing_negotiation_price` DOUBLE NOT NULL,
  `highest_buy_offer_price` DOUBLE NOT NULL,
  `lowest_sell_offer_price` DOUBLE NOT NULL,
  `total_business` BIGINT(20) NOT NULL,
  `total_negotiation` BIGINT(20) NOT NULL,
  `total_volume` DOUBLE NOT NULL,
  `trade_date` DATE NOT NULL,
  `stock_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_stock` (`stock_id` ASC, `trade_date` ASC),
  INDEX `fk_tb_stock_record_tb_company_idx` (`stock_id` ASC),
  CONSTRAINT `fk_tb_stock_record_tb_company`
    FOREIGN KEY (`stock_id`)
    REFERENCES `market_data`.`tb_stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 640194;


-- -----------------------------------------------------
-- Table `market_data`.`tb_index_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `market_data`.`tb_index_record` ;

CREATE TABLE IF NOT EXISTS `market_data`.`tb_index_record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `trade_date` DATE NOT NULL,
  `open` DOUBLE NOT NULL,
  `high` DOUBLE NOT NULL,
  `low` DOUBLE NOT NULL,
  `close` DOUBLE NOT NULL,
  `volume` BIGINT NOT NULL,
  `stock_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tb_index_record_tb_stock1_idx` (`stock_id` ASC),
  CONSTRAINT `fk_tb_index_record_tb_stock1`
    FOREIGN KEY (`stock_id`)
    REFERENCES `market_data`.`tb_stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `market_data`.`tb_benefit_type` (`name`) VALUES ('bonificacao');
INSERT INTO `market_data`.`tb_benefit_type` (`name`) VALUES ('cisao');
INSERT INTO `market_data`.`tb_benefit_type` (`name`) VALUES ('desdobramento');
INSERT INTO `market_data`.`tb_benefit_type` (`name`) VALUES ('grupamento');
INSERT INTO `market_data`.`tb_benefit_type` (`name`) VALUES ('restituicao de capital');
