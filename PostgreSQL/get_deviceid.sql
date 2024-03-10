CREATE OR REPLACE FUNCTION get_deviceid(_device_name VARCHAR(255), _separator CHAR(1))
RETURNS SMALLINT AS $$
DECLARE
    result SMALLINT;
BEGIN
    SELECT id INTO result FROM devices WHERE device_name = _device_name AND fs_separator = _separator LIMIT 1;

    IF result IS NULL THEN
        INSERT INTO devices (device_name, fs_separator) VALUES (_device_name, _separator) RETURNING id INTO result;
    END IF;

    RETURN result;
END;
$$ LANGUAGE plpgsql;