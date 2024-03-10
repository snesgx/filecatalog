/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kudori.FileIndexer;

import java.time.Instant;

/**
 *
 * @author migi
 */
public record FileInfo (byte[] Id, byte[] ParentID, String filePath, long fileSize, boolean isDirectory, Instant modificationDateTime) {
    
}
