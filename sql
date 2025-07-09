CREATE DATABASE veterinaria;
USE veterinaria;

-- Tabla person
CREATE TABLE person (
    document VARCHAR(15) NOT NULL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    numberPhone VARCHAR(20)
);

-- Tabla pet
CREATE TABLE pet (
    id_pet INT AUTO_INCREMENT PRIMARY KEY,
    id_owner VARCHAR(15) NOT NULL,
    name VARCHAR(45) NOT NULL,
    race VARCHAR(45),
    sex VARCHAR(10),
    FOREIGN KEY (id_owner) REFERENCES person(document)
);
