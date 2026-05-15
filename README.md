# Badminton Court Booking System 🏸

A Java Swing-based desktop application for managing badminton court reservations. This system allows users to book courts, view existing bookings, check court availability, and generate revenue reports using a MySQL database.

## 🚀 Features
* **Book Court:** Secure a court by providing customer details and duration.
* **View Bookings:** See a list of all active reservations.
* **Cancel Booking:** Remove a booking and reset the court status to 'Available'.
* **Court Status:** Real-time check of which courts are booked or free.
* **Revenue Report:** Automatically calculate total income from all bookings.

## 🛠️ Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher.
- **MySQL Server:** To store court and booking data.
- **MySQL Connector/J:** JDBC driver to connect Java with MySQL.

## 📋 Database Setup
Before running the application, execute the following SQL commands in your MySQL workbench or terminal:

```sql
CREATE DATABASE badmintondb;
USE badmintondb;

CREATE TABLE courts(
    court_no INT PRIMARY KEY,
    hourly_rate DOUBLE,
    status VARCHAR(20)
);

CREATE TABLE bookings(
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100),
    court_no INT,
    hours INT,
    bill DOUBLE
);

-- Initialize the courts
INSERT INTO courts VALUES
(1, 300, 'Available'),
(2, 300, 'Available'),
(3, 400, 'Available'),
(4, 400, 'Available'),
(5, 500, 'Available');
