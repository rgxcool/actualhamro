-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS `bookings`;
DROP TABLE IF EXISTS `tour_packages`;
DROP TABLE IF EXISTS `destinations`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `users`;

-- Create users table
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255),
  `password` varchar(255) NOT NULL,
  `phone` varchar(255),
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_email` (`email`),
  UNIQUE KEY `UK_username` (`username`)
) ENGINE=InnoDB;

-- Create user_roles table
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint NOT NULL,
  `role` varchar(255),
  KEY `FK_user_id` (`user_id`),
  CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;

-- Create destinations table
CREATE TABLE IF NOT EXISTS `destinations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `highlights` TEXT,
  `image_url` varchar(255),
  `location` varchar(255),
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- Create tour_packages table
CREATE TABLE IF NOT EXISTS `tour_packages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `duration` integer NOT NULL,
  `exclusions` TEXT,
  `image_url` varchar(255),
  `inclusions` TEXT,
  `itinerary` TEXT,
  `name` varchar(255) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `destination_id` bigint,
  PRIMARY KEY (`id`),
  KEY `FK_destination_id` (`destination_id`),
  CONSTRAINT `FK_destination_id` FOREIGN KEY (`destination_id`) REFERENCES `destinations` (`id`)
) ENGINE=InnoDB;

-- Create bookings table
CREATE TABLE IF NOT EXISTS `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_date` date NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `number_of_people` integer NOT NULL,
  `phone` varchar(255) NOT NULL,
  `special_requirements` TEXT,
  `status` varchar(255) NOT NULL,
  `total_amount` decimal(19,2) NOT NULL,
  `tour_start_date` date NOT NULL,
  `tour_package_id` bigint,
  `user_id` bigint,
  PRIMARY KEY (`id`),
  KEY `FK_tour_package_id` (`tour_package_id`),
  KEY `FK_booking_user_id` (`user_id`),
  CONSTRAINT `FK_tour_package_id` FOREIGN KEY (`tour_package_id`) REFERENCES `tour_packages` (`id`),
  CONSTRAINT `FK_booking_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;