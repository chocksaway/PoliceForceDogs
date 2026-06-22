-- schema.sql
-- Database schema for Police Force Dogs application
-- H2 compatible SQL: creates lookup and main tables used by the app

-- Drop in correct order to allow re-run during development
-- Drop join table first (depends on dogs and kennel_characteristics), then main tables
DROP TABLE IF EXISTS dog_kennel_characteristics;
DROP TABLE IF EXISTS dogs;
DROP TABLE IF EXISTS kennel;
DROP TABLE IF EXISTS dog_leaving_reasons;
DROP TABLE IF EXISTS dog_status;
DROP TABLE IF EXISTS kennel_characteristics;
DROP TABLE IF EXISTS police_force;

-- Police force lookup table
CREATE TABLE police_force (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Kennelling characteristic lookup table
CREATE TABLE kennel_characteristics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Dog status lookup table (e.g. In Training, In Service, Retired, Left)
CREATE TABLE dog_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100),
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Dog leaving reasons lookup table (e.g. Transferred, Died, Re-housed...)
CREATE TABLE dog_leaving_reasons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100),
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Kennel table
CREATE TABLE kennel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Main dogs table
CREATE TABLE dogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    breed VARCHAR(255),
    supplier VARCHAR(255),
    badge_id VARCHAR(100),
    gender VARCHAR(50),
    birth_date DATE,
    date_acquired DATE,
    current_status_id BIGINT,
    leaving_date DATE,
    leaving_reason_id BIGINT,
    kennel_id BIGINT,
    police_force_id BIGINT,
    -- Soft delete flag: records are not physically deleted to preserve audit
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_dogs_status FOREIGN KEY (current_status_id)
        REFERENCES dog_status(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_dogs_leaving_reason FOREIGN KEY (leaving_reason_id)
        REFERENCES dog_leaving_reasons(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_dogs_kennel FOREIGN KEY (kennel_id)
        REFERENCES kennel(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_dogs_police_force FOREIGN KEY (police_force_id)
        REFERENCES police_force(id)
        ON DELETE SET NULL
);

-- Optional indexes to speed common queries
CREATE INDEX idx_dogs_name ON dogs(name);
CREATE INDEX idx_dogs_breed ON dogs(breed);
CREATE INDEX idx_dogs_supplier ON dogs(supplier);
CREATE INDEX idx_dogs_deleted ON dogs(deleted);

-- Many-to-many table linking dogs to kennel characteristics (a dog may have multiple characteristics)
CREATE TABLE dog_kennel_characteristics (
    dog_id BIGINT NOT NULL,
    kennel_characteristic_id BIGINT NOT NULL,
    PRIMARY KEY (dog_id, kennel_characteristic_id),
    CONSTRAINT fk_dkc_dog FOREIGN KEY (dog_id)
        REFERENCES dogs(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_dkc_characteristic FOREIGN KEY (kennel_characteristic_id)
        REFERENCES kennel_characteristics(id)
        ON DELETE CASCADE
);