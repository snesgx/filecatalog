CREATE TABLE fileindex (
    id BINARY(16),
    parent_id BINARY(16),
    device_id SMALLINT,
    file_name VARCHAR(255),
    file_size BIGINT,
    is_directory BOOLEAN,
    modification_date_time DATETIME,
    PRIMARY KEY (id,device_id)
);
