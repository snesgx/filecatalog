CREATE TABLE devices (
    id SERIAL PRIMARY KEY,
    fs_separator CHAR(1) NOT NULL,
    device_name VARCHAR(255)
);
