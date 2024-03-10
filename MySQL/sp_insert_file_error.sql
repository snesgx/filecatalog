DROP PROCEDURE IF EXISTS sp_insert_file_error;

DELIMITER $$

CREATE PROCEDURE sp_insert_file_error(
    IN p_fileindex_id BINARY(16),
    IN p_device_id SMALLINT,
    IN p_error VARCHAR(255)
)
BEGIN
    
    DELETE FROM fileerrors where fileindex_id = p_fileindex_id and device_id= p_device_id;

    INSERT INTO fileerrors (fileindex_id, device_id, error, indexing_date_time)
    VALUES (p_fileindex_id, p_device_id, p_error, UTC_TIMESTAMP());
END $$

DELIMITER ;