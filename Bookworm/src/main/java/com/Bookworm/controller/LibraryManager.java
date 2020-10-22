package com.Bookworm.controller;

import com.Bookworm.model.*;

import java.io.File;

public class LibraryManager {
    public static String pathToFilesDirectory = "./src/main/java/com/Bookworm/files/";

    public static boolean reset(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = reset(new File(dir, children[i]));

                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    public static void modifyBook(Book book) {

    }

    public static void removeBook(String book, String bookshelf) {
        File fileToDelete = new File(pathToFilesDirectory + "/" + bookshelf + "/" +  book);
        File fileToDelete2 = new File(pathToFilesDirectory + "/default/" +  book);
        reset(fileToDelete);
        reset(fileToDelete2);
        Loader.reload();
    }

}
