CREATE DATABASE attendance_db;

USE attendance_db;

CREATE TABLE employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    dept VARCHAR(50)
);

CREATE TABLE attendance (
    att_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT,
    att_date DATE,
    status ENUM('present','absent','late','half-day','leave'),
    FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);
