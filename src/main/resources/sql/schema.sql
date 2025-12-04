-- Удаление старых данных (для переинициализации БД)
DROP TABLE IF EXISTS tour_image CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS favorite CASCADE;
DROP TABLE IF EXISTS program_tour CASCADE;
DROP TABLE IF EXISTS service_tour CASCADE;
DROP TABLE IF EXISTS application_tour CASCADE;
DROP TABLE IF EXISTS tour_category CASCADE;
DROP TABLE IF EXISTS tour CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS operator CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS general_status CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;

-- Создание ENUM-типов
CREATE TYPE user_role AS ENUM ('OPERATOR', 'USER', 'ADMIN');
CREATE TYPE general_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED');

-- Создание таблиц
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(63) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    "role" user_role NOT NULL DEFAULT 'USER'
);

CREATE TABLE operator (
    user_id INT PRIMARY KEY,
    company_name VARCHAR(63),
    description VARCHAR(1023),
    status general_status NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    title VARCHAR(63) UNIQUE NOT NULL
);

CREATE TABLE tour (
    id SERIAL PRIMARY KEY,
    title VARCHAR(63) NOT NULL,
    operator_id INT NOT NULL,
    destination VARCHAR(63) NOT NULL,
    description VARCHAR(1023) NOT NULL,
    duration SMALLINT NOT NULL,
    FOREIGN KEY (operator_id) REFERENCES operator(user_id) ON DELETE CASCADE,
    UNIQUE (title, operator_id)
);

CREATE TABLE tour_category (
    tour_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (tour_id, category_id),
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE application_tour (
    id SERIAL PRIMARY KEY,
    tour_id INT NOT NULL,
    user_id INT NOT NULL,
    created_time TIMESTAMPTZ NOT NULL DEFAULT now(),
    status general_status NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE
);

CREATE TABLE service_tour (
    id SERIAL PRIMARY KEY,
    tour_id INT NOT NULL,
    title VARCHAR(63) NOT NULL,
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    UNIQUE (tour_id, title)
);

CREATE TABLE program_tour (
    id SERIAL PRIMARY KEY,
    tour_id INT NOT NULL,
    title VARCHAR(63) NOT NULL,
    description VARCHAR(255),
    day_number SMALLINT NOT NULL CHECK (day_number >= 1),
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    UNIQUE (tour_id, day_number)
);

CREATE TABLE favorite (
    tour_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (tour_id, user_id),
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    text VARCHAR(1023) NOT NULL,
    user_id INT NOT NULL,
    tour_id INT NOT NULL,
    created_time TIMESTAMPTZ DEFAULT NOW(),
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tour_image (
    tour_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_main BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (tour_id) REFERENCES tour(id) ON DELETE CASCADE,
    PRIMARY KEY (tour_id, image_url)
);

-- Создание индексов
CREATE UNIQUE INDEX uq_application_active
    ON application_tour (user_id, tour_id)
    WHERE status IN ('PENDING', 'APPROVED');

CREATE INDEX idx_tour_image_tour_id ON tour_image(tour_id);

-- Начальные данные
INSERT INTO category (title) VALUES ('Природа');
INSERT INTO category (title) VALUES ('Горы');
INSERT INTO category (title) VALUES ('Озера');
INSERT INTO category (title) VALUES ('Культура');
INSERT INTO category (title) VALUES ('Активный отдых');
