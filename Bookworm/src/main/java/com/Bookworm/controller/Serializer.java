package com.Bookworm.controller;
import com.Bookworm.model.Book;

import java.io.*;
import java.nio.file.*;

//this class creates a directory where to save all the files
public class Serializer {
    public static String pathToFilesDirectory = "./src/main/java/com/Bookworm/files/";

    //main method for test purposes
    public static void  main(String[] args) throws IOException {
        Book book = new Book("test", "random", "s", null,"test", 1, "ss");
        Downloader.saveBook(book, "Bookshelf3");
        Loader.createBookshelves();
        LibraryManager.removeBook("random", "Bookshelf3");
        //LibraryManager.reset(new File(pathToFilesDirectory));
    }

    //saves a book in a given bookshelf. It creates the folders they do not exist
    public static void serializeBook(Book book, String bookshelfName, int avoidInfiniteLoop) {
        try
        {
            //Saving of object in a file
            String filename = pathToFilesDirectory + bookshelfName + "/" + book.getName();
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(book);

            out.close();
            file.close();
            //System.out.println("Object has been serialized");
        }

        catch (FileNotFoundException e) {
            Path path = Paths.get(pathToFilesDirectory + bookshelfName);
            try {
                Files.createDirectories(path);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            serializeBook(book, bookshelfName, 0);
        }
        catch(IOException ex)
        {
                System.out.println("IOException is caught - " + ex);
        }

    }
    //the method deserializeBook() created the Book object from a file
    public static Book deserializeBook(String filename, String bookshelfName) {
        Book book = new Book();
        try
        {
            // Reading the object from a file
            String path = pathToFilesDirectory + bookshelfName + "/" + filename;
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object

            book = (Book)in.readObject();

            in.close();
            file.close();
            // System.out.println("Book \"" + book.getName() + "\" has been deserialized ");
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught - " + ex.getMessage());
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return book;
    }
}
