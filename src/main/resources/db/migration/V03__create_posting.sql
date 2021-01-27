CREATE TABLE posting
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(50)    NOT NULL,
    due_date    DATE           NOT NULL,
    payday      DATE,
    value       DECIMAL(10, 2) NOT NULL,
    observation VARCHAR(100),
    type        VARCHAR(20)    NOT NULL,
    category_id INTEGER         NOT NULL,
    person_id   INTEGER         NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (person_id) REFERENCES person (id)
);


