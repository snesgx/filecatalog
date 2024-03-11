/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kudori.FileIndexer;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author migi
 */
@Repository
public class FileIndexerRepositoryImpl implements FileIndexerRepository {
 
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void deletePath(short deviceId, byte[] id) {
        // Set up the stored procedure call
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("delete_path")
                .declareParameters(
                        new SqlParameter("_device_id", Types.SMALLINT),
                        new SqlParameter("_id", Types.BINARY)
                );

        // Set up the input parameters
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("_device_id", deviceId);
        inParams.put("_id", id);

        // Execute the stored procedure
        Map<String, Object> result = simpleJdbcCall.execute(inParams);
    } 
 
    @Override
    public void saveSingle(int DeviceID, FileInfo fileInfo) { //BATCH
        jdbcTemplate.update("INSERT INTO fileindex (id, parent_id, device_id, file_name, file_size, is_directory, modification_date_time) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?)",
                         fileInfo.Id(),
                        fileInfo.ParentID(),
                        DeviceID,
                        fileInfo.filePath(),
                        fileInfo.fileSize(),
                        fileInfo.isDirectory(),
                        Timestamp.from(fileInfo.modificationDateTime()));
    }    
    
    @Override
    @Transactional
    public void saveAll(int DeviceID, List<FileInfo> fileList) { //BATCH
        jdbcTemplate.batchUpdate("INSERT INTO fileindex (id, parent_id, device_id, file_name, file_size, is_directory, modification_date_time) " +
          "VALUES (?, ?, ?, ?, ?, ?, ?)",
                fileList,
                1000,
            (PreparedStatement ps, FileInfo file) -> {
                        ps.setBytes(1, file.Id());
                        ps.setBytes(2, file.ParentID());
                        ps.setInt(3, DeviceID);
                        ps.setString(4, file.filePath());
                        ps.setLong(5,file.fileSize());
                        ps.setBoolean(6,file.isDirectory());
                        ps.setTimestamp(7,Timestamp.from(file.modificationDateTime()));
                    });
    }

    @Override
    @Transactional
    public void saveFileError(int DeviceID, byte[] fileID, String errormsg) {
        jdbcTemplate.update("call sp_insert_file_error (?, ?, ?)",
                fileID,
                DeviceID,
                errormsg);
    }
    
    @Override
    public short getDeviceID(String hostname, String separator){
        return jdbcTemplate.queryForObject("select get_deviceid(?, ?)", short.class, hostname, separator);
    }

    @Override
    public boolean elementExists(short DeviceID, byte[] fileID){
        try {
            return jdbcTemplate.queryForObject("select count(*) from fileindex where device_id=? and id=?", Integer.class, DeviceID, fileID) > 0;
        } catch (Exception ex) {
            return false;
        }
    }    
    
    @Override
    public List<Map<String,Object>> getSummary(){
        return jdbcTemplate.queryForList("call sp_summary()");
    } 
    
}
