/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kudori.FileIndexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author migi
 */
@Component
public class IndexingEngine {
  
        @Autowired
        FileIndexerRepository repository;
    
        private AtomicInteger itemsCount = new AtomicInteger(0);
        private final AtomicBoolean isIndexing = new AtomicBoolean(false);
        
        String hostname = "";
        
        //Initializing variables, such as hostname
        public IndexingEngine(){
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                Logger.getLogger(IndexingEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public String startIndexing(String path) {
            
                itemsCount.set(0);
                isIndexing.set(true);
            
                try {
                    
                    System.out.println("Hostname: " + hostname);
                    
                    long startTime = System.currentTimeMillis();                    
                    int count = listAndInsertFilesUsingFilesList(path);  
                    
                    isIndexing.set(false);
                    return String.valueOf(count) + " elements, time taken: " + formatDuration(System.currentTimeMillis() - startTime);
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                    isIndexing.set(false);
                    return "Error"; 
                }
                
        }
    
    
        public int listAndInsertFilesUsingFilesList(String dir) throws IOException {
            
            List<FileInfo> result = new ArrayList<>();
            
            Path targetPath = Paths.get(dir);

            FileSystem fs = targetPath.getFileSystem();

            short DeviceID = repository.getDeviceID(hostname, fs.getSeparator());

            //Ensuring that all the parents of the given path, exist:
            ensureAllParents(DeviceID, targetPath);
            
            //Deleting existing path and children
            repository.deletePath(DeviceID,getMD5HashAsBytes(targetPath.toString()));
            
            Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    addElement(file, attrs);
                    return FileVisitResult.CONTINUE;                   
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    addElement(dir, attrs);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    addElement(file,Files.readAttributes(file, BasicFileAttributes.class));
                    repository.saveFileError(DeviceID, getMD5HashAsBytes(file.toString()), exc.toString());
                    return FileVisitResult.CONTINUE;
                }
                
                //Add the element to the DB
                public void addElement(Path file, BasicFileAttributes attrs){

                    result.add(new FileInfo( getMD5HashAsBytes(file.toString()),
                                    file.getParent() != null ? getMD5HashAsBytes(file.getParent().toString()) : null,
                                    file.getFileName() != null ? file.getFileName().toString() : "",
                                    attrs.size(),
                                    attrs.isDirectory(),
                                    attrs.lastModifiedTime().toInstant()));

                    if (result.size() >= 1000) { //Partial insertion of results, this avoids overusing RAM
                        repository.saveAll(DeviceID, result);
                        result.clear();
                        Logger.getLogger("general").log(Level.ALL, itemsCount.toString());
                    }                    
                    
                    itemsCount.incrementAndGet();
                   
                }
                
            });
            
                
            if (!result.isEmpty()) { //This is needed because we are using blocks of 1000
                   repository.saveAll(DeviceID, result);
            }            
            
            return itemsCount.get();

    
        }
        
        //Checking all the parents all the way to the root
        private void ensureAllParents(short DeviceID, Path startingPath) {
            Path pathToCheck = startingPath.getParent();
            
            while (pathToCheck != null) {
                
                byte[] md5OfFile = getMD5HashAsBytes(pathToCheck.toString());
                
                if (!repository.elementExists(DeviceID, md5OfFile)) {
                    
                    BasicFileAttributes attrs = null;
                    try {
                        attrs = Files.readAttributes(pathToCheck, BasicFileAttributes.class);
                    } catch (IOException ex) {
                        Logger.getLogger(IndexingEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    repository.saveSingle(DeviceID,new FileInfo( md5OfFile,
                                    pathToCheck.getParent() != null ? getMD5HashAsBytes(pathToCheck.getParent().toString()) : null,
                                    pathToCheck.getFileName() != null ? pathToCheck.getFileName().toString() : "",
                                    attrs != null ? attrs.size() : -1,
                                    attrs != null ? attrs.isDirectory() : true,
                                    attrs != null ? attrs.lastModifiedTime().toInstant() : null ));
                }
                pathToCheck = pathToCheck.getParent();
            }

        }
        
        //UUID has the same size as MD5 sum, so we use as an ID, 
        //  it also makes easier to reconstruct parts of the file system tree, because the ID of the parent folder is always the same
        private byte[] getMD5HashAsBytes(String string) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(string.getBytes());
                return md.digest();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(IndexingEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return null;
        }        
 
        public static String formatDuration(long milliseconds) {
            Duration duration = Duration.ofMillis(milliseconds);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart(); 
            long seconds = duration.toSecondsPart(); 

            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }        
        
        
        public AtomicInteger getItemsCount() {
            return itemsCount;
        }        
        
}
