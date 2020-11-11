package com.Bookworm.controller;

import com.Bookworm.model.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

// implements StorageManager
public class FileManager {
    public static String pathToFilesDirectory = "./src/main/java/com/Bookworm/files/";

    //@Override
    public void reload() {
        loadBookshelves();
    }

    //@Override
    public ArrayList<Book> getAuthors(String author) {
        return null;
    }

    //@Override
    public ArrayList<Book> getBooksByTag(String tag) {
        return null;
    }

    //saves book in the files directory, both in default bookshelf and in the selected bookshelf, if specified
    //@Override
    public void insertBook(Book book, String bookshelf) {
        if (bookshelf == "") {
            Serializer.serializeBook(book, "default", 1);
        } else {
            Serializer.serializeBook(book, bookshelf, 1);
            Serializer.serializeBook(book, "default", 1);
        }
    }

    // creates all bookshelves objects and populates them with their books
    //@Override
    public ArrayList<Bookshelf> loadBookshelves() {
        String[] bookshelvesNames = getBookshelves();
        ArrayList<Bookshelf> bookshelves = new ArrayList();
        for (int i = 0; i < bookshelvesNames.length; i++) {
            Bookshelf newBookshelf = new Bookshelf(bookshelvesNames[i], "", new ArrayList<Book>());
            newBookshelf.setBooks(readBookshelfBooks(newBookshelf.getName()));
            bookshelves.add(newBookshelf);
            System.out.println();
            System.out.println(newBookshelf.getName() + ":");
            for (Book book:newBookshelf.getBooks()) {
                System.out.println("\t" + book.getName());
            }
        }
        return bookshelves;
    }

    //@Override
    public void deleteBook(String book, String bookshelf) {
        File fileToDelete = new File(pathToFilesDirectory + "/" + bookshelf + "/" +  book);
        File fileToDelete2 = new File(pathToFilesDirectory + "/default/" +  book);
        resetSystem(fileToDelete);
        resetSystem(fileToDelete2);
        reload();
    }

    //@Override
    public void addTag(Book book, String bookshelf, String newTag) {
        ArrayList<Tag> tags = book.getTags();
        tags.add(new Tag(newTag));
        book.setTags(tags);
    }

    //deletes all bookshelves directories and serialized books
    //@Override
    public boolean resetSystem(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = resetSystem(new File(dir, children[i]));

                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
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

}
