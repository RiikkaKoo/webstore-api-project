-- --------------------------------------------------------
-- Verkkotietokone:              127.0.0.1
-- Palvelinversio:               11.5.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Versio:              12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS web_store;
CREATE DATABASE web_store;

USE web_store;

-- Dumping structure for taulu webstore.contacts
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `reference` char(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

-- Dumping structure for taulu webstore.customeraddresses
CREATE TABLE IF NOT EXISTS `customeraddresses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL UNIQUE,
  `street_address` varchar(255) NOT NULL,
  `postal_code` varchar(20) NOT NULL,
  `city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_customeraddress_customer` (`customer_id`),
  CONSTRAINT `fk_customeraddress_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

CREATE INDEX idx_customeraddresses_country
ON customeraddresses(country);

-- Dumping structure for taulu webstore.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL UNIQUE,
  `phone` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

CREATE INDEX idx_customers_lastname
ON customers(last_name);


-- Dumping structure for taulu webstore.orderitems
CREATE TABLE IF NOT EXISTS `orderitems` (
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `fk_orderitem_product` (`product_id`),
  CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_orderitem_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

-- Dumping structure for taulu webstore.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `DTYPE` varchar(50),
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `order_date` datetime NOT NULL DEFAULT current_timestamp(),
  `status` varchar(50) NOT NULL DEFAULT 'NEW',
  PRIMARY KEY (`id`),
  KEY `fk_order_customer` (`customer_id`),
  CONSTRAINT `fk_order_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

-- Dumping structure for taulu webstore.orders
CREATE TABLE IF NOT EXISTS `pickup_orders` (
  `id` int(11) NOT NULL,
  `final_pickup_date` date NOT NULL,
  `pickup_location` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_porder_id` (`id`),
  CONSTRAINT `fk_porder_id` FOREIGN KEY (`id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

-- Dumping structure for taulu webstore.orders
CREATE TABLE IF NOT EXISTS `delivery_orders` (
  `id` int(11) NOT NULL,
  `delivery_date` datetime DEFAULT NULL,
  `street_address` varchar(255) NOT NULL,
  `postal_code` varchar(20) NOT NULL,
  `city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_dorder_id` (`id`),
  CONSTRAINT `fk_dorder_id` FOREIGN KEY (`id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

CREATE INDEX idx_orders_orderdate
ON orders(order_date);

CREATE INDEX idx_orders_status
ON orders(status);

-- Dumping structure for taulu webstore.productcategories
CREATE TABLE IF NOT EXISTS `productcategories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

-- Dumping structure for taulu webstore.products
CREATE TABLE IF NOT EXISTS `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int(11) NOT NULL DEFAULT 0,
  `category_id` int(11) DEFAULT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `active` varchar(50) NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`),
  KEY `fk_product_category` (`category_id`),
  KEY `fk_product_supplier` (`supplier_id`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `productcategories` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_product_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

CREATE INDEX idx_products_price
ON products(price);

CREATE INDEX idx_products_stock
ON products(stock_quantity);


-- Dumping structure for taulu webstore.supplieraddresses
CREATE TABLE IF NOT EXISTS `supplieraddresses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `supplier_id` int(11) NOT NULL UNIQUE,
  `street_address` varchar(255) NOT NULL,
  `postal_code` varchar(20) NOT NULL,
  `city` varchar(100) NOT NULL,
  `country` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplieraddress_supplier` (`supplier_id`),
  CONSTRAINT `fk_supplieraddress_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.

CREATE INDEX idx_supplieraddresses_country
ON supplieraddresses(country);

-- Dumping structure for taulu webstore.suppliers
CREATE TABLE IF NOT EXISTS `suppliers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `contact_name` varchar(100) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) WITH SYSTEM VERSIONING ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tietojen vientiä ei oltu valittu.


CREATE TABLE IF NOT EXISTS `daily_sales` (
`id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
`report_date` date NOT NULL,
`orders_total` int(11) NOT NULL DEFAULT 0,
`sales_total` decimal(10,2) NOT NULL DEFAULT 0
);


/* Trigger */
DELIMITER //

CREATE TRIGGER validate_stock_quantity
BEFORE UPDATE ON products
FOR EACH ROW
BEGIN
    IF NEW.stock_quantity < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Invalid stock quantity';
    END IF;
END//

DELIMITER ;



/* Event */
DELIMITER //

CREATE EVENT `log_daily_sales` 
ON SCHEDULE EVERY 1 DAY
STARTS '2026-09-10 00:00:00'
DO
BEGIN
    INSERT INTO `daily_sales`
        (`report_date`, `orders_total`, `sales_total`) 
    VALUES (
        CURDATE() - INTERVAL 1 DAY,

        (
            SELECT COUNT(*)
            FROM orders
            WHERE order_date >= CURDATE() - INTERVAL 1 DAY
              AND order_date < CURDATE()
              AND status != 'CANCELLED'
        ),

        (
            SELECT COALESCE(SUM(orderitems.unit_price * orderitems.quantity), 0)
            FROM orderitems 
            INNER JOIN orders
                ON orderitems.order_id = orders.id 
            WHERE orders.order_date >= CURDATE() - INTERVAL 1 DAY
              AND orders.order_date < CURDATE()
              AND orders.status != 'CANCELLED'
        )
    );
END//

DELIMITER ;

/* View */
CREATE VIEW stock_suppliers AS
SELECT 
products.id AS product_id, 
products.name AS product_name, 
products.stock_quantity, 
suppliers.id AS supplier_id, 
suppliers.name AS supplier, 
suppliers.contact_name, 
suppliers.phone, 
suppliers.email, 
supplieraddresses.street_address, 
supplieraddresses.city, 
supplieraddresses.country
FROM products
INNER JOIN suppliers ON products.supplier_id = suppliers.id
INNER JOIN supplieraddresses ON suppliers.id = supplieraddresses.supplier_id
ORDER BY suppliers.id;

/* View */
CREATE VIEW order_items AS
SELECT 
orders.id AS order_id,
orders.customer_id,
products.id AS product_id, 
products.name AS product_name, 
orderitems.quantity,
orderitems.unit_price,
(orderitems.quantity * orderitems.unit_price) AS total
FROM orders
INNER JOIN orderitems ON orders.id = orderitems.order_id
INNER JOIN products ON orderitems.product_id = products.id
ORDER BY orders.id;

/* View */
CREATE VIEW orders_stats AS
SELECT
    COUNT(DISTINCT delivery_orders.id) AS delivery_orders,
    delivery_orders.country,
    SUM(orderitems.unit_price * orderitems.quantity) AS sales
FROM delivery_orders
JOIN orders
    ON orders.id = delivery_orders.id
LEFT JOIN orderitems
    ON orderitems.order_id = delivery_orders.id
WHERE orders.status != "CANCELLED"
GROUP BY delivery_orders.country
ORDER BY sales DESC;


SET GLOBAL event_scheduler = ON;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
