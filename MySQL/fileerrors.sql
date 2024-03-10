CREATE TABLE fileerrors (
    fileindex_id BINARY(16),
    device_id SMALLINT,
    error VARCHAR(255),
    indexing_date_time DATETIME,
    PRIMARY KEY (fileindex_id,device_id)
);
