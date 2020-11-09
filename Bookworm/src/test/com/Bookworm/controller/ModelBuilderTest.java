package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.model.Tag;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


class ModelBuilderTest {
    // copy and paste DatabaseManager in DatabaseManagerTest and change name to the database to create
    // a new database only for tests
    // use methods of DatabaseManagerTest
    // i think insertBook is not working
    DatabaseManagerTest dbtest = DatabaseManagerTest.getInstance();

    @Test
    void setUp() throws SQLException, ClassNotFoundException {
        Book book1 = new Book("A cute lil' book", "The Great Gatsby", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");
        Book book2 = new Book("A thicc big book", "War and Peace", null, "Lev Tolstoj", 1, null, "https://cataas.com/cat/says/meow?size=50&color=green");
        Book book3 = new Book("A damn epic book", "The Lord of the Rings", null, "J. R. R. Tolkien", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green");

        dbtest.insertBook(book1, "FirstBookshelf");
        dbtest.insertBook(book2, "FirstBookshelf");
        dbtest.insertBook(book3, "FirstBookshelf");
    }

    @Test
    void makeBook() throws SQLException, ClassNotFoundException {
        List<Book> bookList = dbtest.getBooks();
        if (bookList == null) {
            fail("List of book cannot be null");
        }
    }



    @Test
    void makeBookshelves() throws SQLException, ClassNotFoundException {
        List<Bookshelf> bookshelfList = dbtest.getBookShelves();
        if (bookshelfList == null) {
            fail("The list of bookshelf cannot be null");
        }
    }

    @Test
    void makeTags() throws SQLException {
        List<Tag> tagList = dbtest.getTags();
        if (tagList == null) {
            fail("The list of tags cannot be null");
        }
    }

    void tearDown() {

    }
}