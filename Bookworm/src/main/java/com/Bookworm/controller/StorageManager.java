package com.Bookworm.controller;

import com.Bookworm.model.*;

import java.io.File;
import java.util.ArrayList;

public interface StorageManager {
    public void saveBook(Book book, String bookshelf);
    public void removeBook(String book, String bookshelf);
    public void modifyBook(String book, String bookshelf);
    public void addTag(Book book, String bookshelf, String newTag);
    public ArrayList<Bookshelf> loadBookshelves();
    public void reload();

    public boolean resetSystem(File dir);
    
    //not implemented
    public ArrayList<Book> getBooksByAuthor(String author);
    //not implemented
    public ArrayList<Book> getBooksByTag(String tag);


}
