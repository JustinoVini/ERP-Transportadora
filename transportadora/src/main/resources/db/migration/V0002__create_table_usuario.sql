CREATE TABLE transportadora_3.users (
    id VARCHAR(36) NOT NULL,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
);
