DROP FUNCTION IF EXISTS get_filefullpath;
DELIMITER $$
CREATE FUNCTION get_filefullpath(_device_id SMALLINT, _id BINARY(16))
RETURNS TEXT
BEGIN
    DECLARE fullpath TEXT;
	DECLARE filename VARCHAR(255);
	DECLARE nextId BINARY(16);
	DECLARE sep CHAR(1);   

	select fs_separator INTO sep from devices where id=_device_id;

	IF sep IS NULL THEN RETURN NULL;
	END IF;

   	SET nextId=_id;

    REPEAT
		
    	SET filename = NULL;
    
    	select file_name, parent_id INTO filename, nextId from fileindex where id=nextId and device_id=_device_id;

    	IF fullpath IS NULL THEN SET fullpath = filename;
    	ELSEIF filename IS NULL THEN RETURN CONCAT(sep,fullpath);
    	ELSE set fullpath = CONCAT(filename , sep , fullpath);
    	END IF;
    
    	IF CHAR_LENGTH(fullpath) > 200 THEN RETURN fullpath;
    	END IF;
    
    UNTIL nextId IS NULL END REPEAT;    
   
    RETURN fullpath;
END 
$$
DELIMITER ;