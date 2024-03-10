DROP FUNCTION IF EXISTS get_deviceid;
DELIMITER $$
CREATE FUNCTION get_deviceid(_device_name VARCHAR(255), _separator CHAR(1))
RETURNS SMALLINT
BEGIN
    DECLARE result SMALLINT;

    select id INTO result from devices where device_name=_device_name and fs_separator=_separator limit 1;
	
   	If result IS NULL THEN
   		INSERT INTO devices (device_name, fs_separator) VALUES(_device_name, _separator);	
   		select id INTO result from devices where device_name=_device_name and fs_separator=_separator limit 1;
   	END IF;
   
    RETURN result;
END 
$$
DELIMITER ;