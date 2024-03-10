CREATE OR REPLACE FUNCTION delete_path(_device_id SMALLINT, _id UUID)
RETURNS INT AS $$
DECLARE
    result INT := 0;
BEGIN
    DELETE FROM fileindex WHERE device_id = _device_id AND (id = _id OR parent_id = _id);
    result := result + FOUND_ROWS();
    
    DELETE FROM fileindex a 
    USING fileindex b 
    WHERE a.device_id = _device_id AND b.device_id = _device_id 
    AND a.parent_id = b.id AND b.id IS NULL; -- deleting orphans
    result := result + FOUND_ROWS();
    
    RETURN result;
END;
$$ LANGUAGE plpgsql;