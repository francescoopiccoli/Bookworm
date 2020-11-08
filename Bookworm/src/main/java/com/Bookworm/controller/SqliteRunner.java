package com.Bookworm.controller;

import com.Bookworm.model.Book;

public class SqliteRunner {

    public static void main(String[] args) {

        DatabaseManager test = DatabaseManager.getInstance();
        try {
            Book bo = new Book("A cute lil' book", "prova", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");
            test.insertBook(bo, null);
            //DatabaseManager.con.close();
            //Bookshelf b = new Bookshelf("lista", "ciao", null);
           // test.insertBookshelf(b);
            for (Book book : test.getBooks()) {
                System.out.println(book.getAuthor() + " - " + book.getName());
            }

            for(String author : test.getAuthors()) {
                System.out.println(author);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}