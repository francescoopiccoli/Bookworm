package com.Bookworm.controller;

import com.Bookworm.model.Book;

import java.sql.ResultSet;
import java.util.List;

public class SqliteRunner {

    public static void main(String[] args) {

        DatabaseManager test = new DatabaseManager();
        try {
          // Book b = new Book("A cute lil' book", "The Great Gatsby", "", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");
          // test.insertBook(b);
            for (Book book : test.getBooks()) {
                System.out.println(book.getAuthor() + " - " + book.getName());
            }

            for(String author : test.getAuthors()) {
                System.out.println(author);
            }
            DatabaseManager.con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}