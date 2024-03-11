USE kudori_local;


Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0x5650F750AD80220DD51B51041D900005);
Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0xFC4020093EF9E6A84919D684B9385DB7);
Select HEX(parent_id), fileindex.* from fileindex where device_id=3 and (id=0x362E925C54E2026EF7AF7A5029C3E243);

Delete from fileindex where device_id=3 and (id=0xFC4020093EF9E6A84919D684B9385DB7 or parent_id=0xFC4020093EF9E6A84919D684B9385DB7);

Delete a from fileindex a left join fileindex b on a.parent_id=b.id and a.device_id=b.device_id where a.device_id=3 and b.id is null;

Select HEX(a.id),a.*,b.* from fileindex a left join fileindex b on a.parent_id=b.id and a.device_id=b.device_id where a.device_id=3 and b.id is null;


Select get_filefullpath(3,id),HEX(id) from fileindex where device_id=3 and id=0x4AB7C881DCFDFCD65B7C8745EB276D2E ;

Select * from fileindex a left join fileindex b on a.parent_id=b.id and a.device_id=b.device_id where a.device_id=1 and b.id is null;

Select get_filefullpath(3,0xC399C42CF5515A074713AAC9BA039F4A);

Select file_size,get_filefullpath(device_id,id) from fileindex where file_size in (
Select file_size from fileindex where file_size > 1600000 group by file_size having count(*) > 1
) order by file_size desc;

select device_id,file_name,parent_id,id from fileindex where id=0x6666CD76F96956469E7BE39D750CC7D9;

Select * from fileerrors a, fileindex f where a.fileindex_id=f.id;

Select * from fileerrors;

Select count(*) from fileindex where device_id =3; 
/*
Delete from fileerrors;
Delete from fileindex;
Delete from fileindex f where device_id =3;
*/
2317359
2330359