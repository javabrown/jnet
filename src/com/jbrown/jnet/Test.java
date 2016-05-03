package com.jbrown.jnet;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import com.jbrown.jnet.utils.FileHelper;

public class Test {
  public static void main(String args[]) throws Exception {
     System.out.println("Hello");
     File a = new File("C:/Users/rkhan/Desktop/a/");
     File b = new File("C:/Users/rkhan/Desktop/b/");
     
     Path from = Paths.get("C:/Users/rkhan/Desktop/a/");
     Path to = Paths.get("C:/Users/rkhan/Desktop/b/");
     
     //==
     try {
       WatchService watcher = from.getFileSystem().newWatchService();
       from.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, 
       StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

       WatchKey watckKey = watcher.take();

       while(true){
         List<WatchEvent<?>> events = watckKey.pollEvents();
         for (WatchEvent event : events) {
              if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                  System.out.println("Created: ");
                  System.out.println( FileHelper.areInSync(a, b) );
                  FileHelper.synchronize(a, b, true);
              }
              if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                  System.out.println("Delete: ");
                  System.out.println( FileHelper.areInSync(a, b) );
                  FileHelper.synchronize(a, b, true);
              }
              if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                  System.out.println("Modify: ");
                  System.out.println( FileHelper.areInSync(a, b) );
                  FileHelper.synchronize(a, b, true);
              }
          }
       }
    } catch (Exception e) {
        System.out.println("Error: " + e.toString());
    }     
     //==
     
     System.out.println( FileHelper.areInSync(a, b) );
     FileHelper.synchronize(a, b, true);
     
  }
}
