-- MySQL Script generated by MySQL Workbench
-- Fri Nov 29 21:01:48 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ruu_movies
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ruu_movies
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ruu_movies` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ruu_movies` ;

-- -----------------------------------------------------
-- Table `ruu_movies`.`franchise`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`franchise` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `details` VARCHAR(45) NULL DEFAULT NULL,
  `group_link` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`films`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`films` (
  `film_id` VARCHAR(16) NOT NULL,
  `film_name` VARCHAR(255) NOT NULL,
  `producer` VARCHAR(45) NOT NULL,
  `link` VARCHAR(32) NOT NULL,
  `year` YEAR NULL DEFAULT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `franchise_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`film_id`),
  INDEX `fk_films_franchise1_idx` (`franchise_id` ASC) VISIBLE,
  CONSTRAINT `fk_films_franchise1`
    FOREIGN KEY (`franchise_id`)
    REFERENCES `ruu_movies`.`franchise` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`genres`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`genres` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`films_has_genres`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`films_has_genres` (
  `films_film_id` VARCHAR(16) NULL DEFAULT NULL,
  `genres_id` INT NULL DEFAULT NULL,
  INDEX `fk_films_has_genres_genres1_idx` (`genres_id` ASC) VISIBLE,
  INDEX `fk_films_has_genres_films1_idx` (`films_film_id` ASC) VISIBLE,
  CONSTRAINT `fk_films_has_genres_films1`
    FOREIGN KEY (`films_film_id`)
    REFERENCES `ruu_movies`.`films` (`film_id`),
  CONSTRAINT `fk_films_has_genres_genres1`
    FOREIGN KEY (`genres_id`)
    REFERENCES `ruu_movies`.`genres` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`tv_series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`tv_series` (
  `group_id` VARCHAR(32) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `group_link` VARCHAR(32) NOT NULL,
  `year` YEAR NULL DEFAULT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`genres_has_tv_series`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`genres_has_tv_series` (
  `genres_id` INT NULL DEFAULT NULL,
  `tv_series_group_id` VARCHAR(32) NULL DEFAULT NULL,
  INDEX `fk_genres_has_tv_series_tv_series1_idx` (`tv_series_group_id` ASC) VISIBLE,
  INDEX `fk_genres_has_tv_series_genres1_idx` (`genres_id` ASC) VISIBLE,
  CONSTRAINT `fk_genres_has_tv_series_genres1`
    FOREIGN KEY (`genres_id`)
    REFERENCES `ruu_movies`.`genres` (`id`),
  CONSTRAINT `fk_genres_has_tv_series_tv_series1`
    FOREIGN KEY (`tv_series_group_id`)
    REFERENCES `ruu_movies`.`tv_series` (`group_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`groups` (
  `group_id` VARCHAR(32) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`cgroup_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`redis_cache`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`redis_cache` (
  `cache_id` BIGINT NOT NULL AUTO_INCREMENT,
  `cache_key` VARCHAR(255) NOT NULL,
  `cache_value` TEXT NULL DEFAULT NULL,
  `related_table` VARCHAR(64) NULL DEFAULT NULL,
  `related_id` VARCHAR(64) NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` TIMESTAMP NULL DEFAULT NULL,
  `status` ENUM('active', 'expired', 'deleted') NULL DEFAULT 'active',
  PRIMARY KEY (`cache_id`),
  UNIQUE INDEX `idx_cache_key` (`cache_key` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`torrent_files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`torrent_files` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `torrent_name` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` VARCHAR(255) NULL DEFAULT NULL,
  `user_name` VARCHAR(255) NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`user_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`user_type` (
  `type_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`user` (
  `user_id` VARCHAR(32) NOT NULL,
  `username` VARCHAR(16) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `user_type_type_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_user_user_type_idx` (`user_type_type_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_user_type`
    FOREIGN KEY (`user_type_type_id`)
    REFERENCES `ruu_movies`.`user_type` (`type_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ruu_movies`.`user_has_groups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ruu_movies`.`user_has_groups` (
  `user_user_id` VARCHAR(32) NULL DEFAULT NULL,
  `groups_group_id` VARCHAR(32) NULL DEFAULT NULL,
  INDEX `fk_user_has_groups_groups1_idx` (`groups_group_id` ASC) VISIBLE,
  INDEX `fk_user_has_groups_user1_idx` (`user_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_groups_groups1`
    FOREIGN KEY (`groups_group_id`)
    REFERENCES `ruu_movies`.`groups` (`group_id`),
  CONSTRAINT `fk_user_has_groups_user1`
    FOREIGN KEY (`user_user_id`)
    REFERENCES `ruu_movies`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
