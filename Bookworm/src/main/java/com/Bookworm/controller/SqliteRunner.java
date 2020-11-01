package com.Bookworm.controller;

import com.Bookworm.model.Book;

import java.sql.ResultSet;

public class SqliteRunner {

    public static void main(String[] args) {

        DatabaseManager test = new DatabaseManager();
        ResultSet rs;

        try {
           Book b = new Book("A cute lil' book", "The Great Gatsby", "", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");
           test.insertBook(b);
            rs = test.displayBooks();

            while (rs.next()) {
                System.out.println(rs.getString("name") + " " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}