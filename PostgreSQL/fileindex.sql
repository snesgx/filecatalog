CREATE TABLE fileindex (
    id UUID,
    parent_id UUID,
    device_id SMALLINT,
    file_name VARCHAR(255),
    file_size BIGINT,
    is_directory BOOLEAN,
    modification_date_time TIMESTAMP,
    PRIMARY KEY (id, device_id)
);