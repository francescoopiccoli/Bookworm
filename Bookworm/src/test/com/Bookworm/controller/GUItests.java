package com.Bookworm.controller;

import com.Bookworm.App;
import com.Bookworm.model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GUItests {
    static Book book = new Book("history", "peppa", "null");
    static Book book2 = new Book("history2", "peppa2", "descripion");
    static Book book3 = new Book("history3", "peppa3", "descripion2");
    static Book book4 = new Book("history4", "peppa4", "null3");
    static Book book5 = new Book("history5", "peppa5", "null4");

    @BeforeAll
    public static void setUp() {
        App.openedBooks.add(book);
        App.openedBooks.add(book2);
        book3.setId(2);
        App.openedBooks.add(book3);
        book3.setName("other");
        App.openedBooks.add(book4);
    }

    @Test
    public void hasOpenedBookWithoutId() {
        assertTrue(App.hasOpenedBook(book2));
    }

    @Test
    public void hasOpenedBookWithoutId2() {
        assertFalse(App.hasOpenedBook(book5));
    }


    @Test
    public void hasOpenedBookWithId() {
        assertTrue(App.hasOpenedBook(book3));
    }


    @AfterAll
    public static void tearDown() {
        App.openedBooks.remove(book);
        App.openedBooks.remove(book2);
        App.openedBooks.remove(book3);
        App.openedBooks.remove(book4);
        App.openedBooks.remove(book5);
    }

}
