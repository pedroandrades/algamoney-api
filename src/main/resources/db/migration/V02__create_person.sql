CREATE TABLE person
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL,
    street VARCHAR,
    number VARCHAR,
    complement VARCHAR,
    zip VARCHAR,
    city VARCHAR,
    state VARCHAR
);