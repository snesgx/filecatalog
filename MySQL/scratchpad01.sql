USE kudori_local;


Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0x02B1B85810B4C4BA76A57CE8C6B61987);
Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0xDB330504B58670B108A86B24389A2498);
Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0x362E925C54E2026EF7AF7A5029C3E243);



Select get_filefullpath(3,id),HEX(id) from fileindex where device_id=3 ;

Select * from fileindex a left join fileindex b on a.parent_id=b.id and a.device_id=b.device_id where a.device_id=1 and b.id is null;

Select get_filefullpath(3,0xC399C42CF5515A074713AAC9BA039F4A);

Select file_size,get_filefullpath(device_id,id) from fileindex where file_size in (
Select file_size from fileindex where file_size > 1600000 group by file_size having count(*) > 1
) order by file_size desc;

select device_id,file_name,parent_id,id from fileindex where id=0x6666CD76F96956469E7BE39D750CC7D9;

Select * from fileerrors a, fileindex f where a.fileindex_id=f.id;

Select * from fileerrors;
/*
Delete from fileerrors;
Delete from fileindex f where device_id =3;
*/