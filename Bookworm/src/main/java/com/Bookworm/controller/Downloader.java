package com.Bookworm.controller;

import com.Bookworm.model.Book;

import java.io.IOException;

public class Downloader {

    //saves book in the files directory, both in default bookshelf and in the book's bookshelf
    public static void saveBook(Book book, String bookshelf) {
        if (bookshelf == "") {
            Serializer.serializeBook(book, "default", 1);
        } else {
            Serializer.serializeBook(book, bookshelf, 1);
            Serializer.serializeBook(book, "default", 1);
        }
    }

}
