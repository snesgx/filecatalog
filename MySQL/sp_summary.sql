DROP PROCEDURE IF EXISTS sp_summary;
DELIMITER $$
CREATE PROCEDURE sp_summary()
BEGIN

	select a.fs_separator,a.device_name, count(*) files, format_bytes(sum(file_size)) TotalSize from devices a, fileindex b
	where a.id = b.device_id 
	group by a.fs_separator,a.device_name;

END $$
DELIMITER ;
