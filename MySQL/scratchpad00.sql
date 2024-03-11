USE kudori_local;

/*
drop table fileindex;
drop table devices;
delete from fileindex;
*/
select get_deviceid('ankkes');

select * from devices;

select * from fileindex where device_id=2 order by file_size desc;

select *,get_filefullpath(device_id,id) fullpath from fileindex where device_id=2 order by file_size desc;


select sum(file_size) from fileindex;
select count(*) from fileindex;


select *,get_filefullpath(device_id,id) from fileindex where file_name like '%2016%' order by file_size desc;



CALL delete_path(1,"/");

select device_id,id,modification_date_time, 
file_size,get_filefullpath(device_id,id) fullpath   
from fileindex
order by file_size DESC LIMIT 100;


/* Summary: */
select a.fs_separator,a.device_name, count(*) files, format_bytes(sum(file_size)) TotalSize from devices a, fileindex b
where a.id = b.device_id 
group by a.fs_separator,a.device_name;

Select HEX(id) from fileindex f where device_id =3 and HEX(id) like 'FC4020%' order by id desc;

Select 'HX\xDA\xB5\xB4\xAC\x16\xAD+}'F\x98\xC2S*-3' ;