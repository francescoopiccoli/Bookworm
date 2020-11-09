package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

public class SqliteRunner {

    public static void main(String[] args) {

        DatabaseManager test = DatabaseManager.getInstance();
        try {
            //Book bo = new Book("A cute lil' book", "prova", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");
            //test.insertBook(bo, null);
            //Bookshelf b = new Bookshelf("horrir", "r", null);
            //test.insertBookshelf(b);
            System.out.println(test.getBookShelves());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}