DROP PROCEDURE delete_path;
DELIMITER $$
CREATE PROCEDURE delete_path(_device_id SMALLINT, _id BINARY(16))
BEGIN
	DECLARE result INT;
    Delete from fileindex where device_id=_device_id and (id=_id or parent_id=_id);
   	SET result= result + ROW_COUNT();
    Delete a from fileindex a left join fileindex b on a.parent_id=b.id and a.device_id=b.device_id where a.device_id=_device_id and b.id is null;  /* deleting orphans */
   	SET result= result + ROW_COUNT();
    Select result;
END 
$$
DELIMITER ;