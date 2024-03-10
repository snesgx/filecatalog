/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kudori.FileIndexer;

import java.util.List;
import java.util.Map;

/**
 *
 * @author migi
 */
interface FileIndexerRepository {

    //Deletes a folder and its childs, this allows for partial filesystem refreshes
    void deletePath(short deviceId, byte[] id);
    
    //Insert single File info
    void saveSingle(int DeviceID, FileInfo fileInfo);
    
    //Insert batch of file info
    void saveAll(int DeviceID, List<FileInfo> fileList);
    
    //We need to report errors while walking files and directories
    void saveFileError(int DeviceID, byte[] fileID, String errormsg);
    
    //Getting information for a single file or directory
    FileInfo getSingleElement(short DeviceID, byte[] fileID);
    
    //Getting the device ID assigned by the DB
    short getDeviceID(String hostname, String separator);

    //Getting summary of the data collected
    List<Map<String,Object>> getSummary();
    
}
