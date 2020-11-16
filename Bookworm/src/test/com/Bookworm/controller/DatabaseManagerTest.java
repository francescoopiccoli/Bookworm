package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


class DatabaseManagerTest {
    // copy and paste DatabaseManager in DatabaseManagerTest and change name to the database to create
    // a new database only for tests
    // use methods of DatabaseManagerTest
    // i think insertBook is not working
    DatabaseManager dbtest = DatabaseManager.getInstance();
    Book book1 = new Book("The Great Gatsby", "Francis Scott Fitzgerald", "");
    Book book2 = new Book("War and Peace", "A thicc big book", "Lev Tolstoj");
    Book book3 = new Book("The Lord of the Rings", "J. R. R. Tolkien", "A damn epic book");


    @Test
    void setUp() throws SQLException, ClassNotFoundException {
        dbtest.insertBook(book1, "FirstBookshelf");
        dbtest.insertBook(book2, "FirstBookshelf");
        dbtest.insertBook(book3, "FirstBookshelf");
    }

    @Test
    void getBooks() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBooks(), "List of book cannot be null ");

    }

    /* @Test
   void getRewiew () throws SQLException, ClassNotFoundException {

   }

   @Test
   void getRating () throws SQLException, ClassNotFoundException {

   }
*/
    @Test
    void getAuthors() throws SQLException, ClassNotFoundException {
        List<String> authorList = dbtest.getAuthors();
        if (authorList == null || authorList.isEmpty()) {
            fail("Author List cannot be neither null nor empty");
        }
    }

    @Test
    void getBookshelves() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBookShelves(), "The list of bookshelf cannot be null");
    }

    //@Test
    //void makeTags() throws SQLException {
    //    List<Tag> tagList = dbtest.getTags();
    //   if (tagList == null) {
    //       fail("The list of tags cannot be null");
    //   }
    //}


    @Test
    void tearDown() throws SQLException, ClassNotFoundException {
        dbtest.deleteBook(book1);
        dbtest.deleteBook(book2);
        dbtest.deleteBook(book3);
    }
}