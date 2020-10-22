package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class Loader {
    public static String pathToFilesDirectory = "./src/main/java/com/Bookworm/files/";

    public static void reload() {
        createBookshelves();
    }

    // gets all bookshelves names in files directory
    private static String[] getBookshelves() {
        File file = new File(pathToFilesDirectory);
        String[] directories = file.list(new FilenameFilter() {
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }

    // reads all files inside of a bookshelf and deserializes Book objects
    private static ArrayList<Book> readBookshelfBooks(String bookshelfName) {
        File directoryPath = new File(pathToFilesDirectory + bookshelfName);
        //List of all files and directories
        String contents[] = directoryPath.list();
        ArrayList<Book> books = new ArrayList<>();

        for(int i=0; i<contents.length; i++) {
            books.add(Serializer.deserializeBook(contents[i], bookshelfName));
        }
        return books;
    }

    // creates all bookshelves objects and populates them with their books
    public static ArrayList<Bookshelf> createBookshelves() {
        String[] bookshelvesNames = getBookshelves();
        ArrayList<Bookshelf> bookshelves = new ArrayList();
        for (int i = 0; i < bookshelvesNames.length; i++) {
            Bookshelf newBookshelf = new Bookshelf(bookshelvesNames[i], "", new ArrayList<Book>());
            newBookshelf.setBooks(readBookshelfBooks(newBookshelf.getName()));
            System.out.println();
            System.out.println(newBookshelf.getName() + ":");
            for (Book book:newBookshelf.getBooks()) {
                System.out.println("\t" + book.getName());
            }
        }
        return null;
    }
}
