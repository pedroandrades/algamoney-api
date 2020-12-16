CREATE TABLE category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO category (name) values ('Leisure');
INSERT INTO category (name) values ('Food');
INSERT INTO category (name) values ('Supermarket');
INSERT INTO category (name) values ('Pharmacy');
INSERT INTO category (name) values ('Others');
