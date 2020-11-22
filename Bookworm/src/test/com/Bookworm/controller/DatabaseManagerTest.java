package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseManagerTest {
    // copy and paste DatabaseManager in DatabaseManagerTest and change name to the database to create
    // a new database only for tests
    // use methods of DatabaseManagerTest
    // i think insertBook is not working
    static DatabaseManager dbtest = DatabaseManager.getInstance();
    static Book book1 = new Book("The Great Gatsby", "Francis Scott Fitzgerald", "");
    static Book book2 = new Book("War and Peace", "A thicc big book", "Lev Tolstoj");
    static Book book3 = new Book("The Lord of the Rings", "J. R. R. Tolkien", "A damn epic book");
    static Book book4 = new Book("The Greeeeeat Gatsby", "Non esiste, non inserirmi", "");
    static Bookshelf bookshelf1 = new Bookshelf("bookshelf1", "test");
    static Bookshelf bookshelf2 = new Bookshelf("bookshelf2", "test2");

    @BeforeAll
    public static void setUp() throws SQLException, ClassNotFoundException {
        dbtest.insertBook(book1, "FirstBookshelf");
        dbtest.insertBook(book2, "FirstBookshelf");
        dbtest.insertBook(book3, "FirstBookshelf");
    }

    @Test
    public void getBook() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBook("The Great Gatsby", "Francis Scott Fitzgerald"), "Book has been inserted in setUp");
    }
    @Test
    public void getBook2() throws SQLException, ClassNotFoundException {
        assertNull(dbtest.getBook("does not exist", "Francis Scott Fitzgerald"));
    }

    @Test
    public void getBooks() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBooks(), "List of book cannot be null ");
    }


    @Test
    public void getRewiew() throws SQLException, ClassNotFoundException {
        assertNull(dbtest.getReview(null));
   }

   @Test
   public void getRewiew2() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getReview(book1));
    }

    @Test
    public void getRating() throws SQLException, ClassNotFoundException {
        assertEquals(0, dbtest.getRating(book1));
    }

    @Test
    public void getRatingNull() throws SQLException, ClassNotFoundException {
        assertEquals(-1, dbtest.getRating(null));
    }

    @Test
    public void getAuthors() throws SQLException, ClassNotFoundException {
        List<String> authorList = dbtest.getAuthors();
        if (authorList == null || authorList.isEmpty()) {
            fail("Author List cannot be neither null nor empty, since books have been inserted");
        }
    }

    @Test
    public void getBookshelves() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBookShelves(), "The list of bookshelf cannot be null");
    }

    @Test
    public void bookAlreadySaved() throws SQLException, ClassNotFoundException {
        assertTrue(dbtest.bookAlreadySaved(book1));
    }

    @Test
    public void bookAlreadySaved2() throws SQLException, ClassNotFoundException {
        assertFalse(dbtest.bookAlreadySaved(book4));
    }

    //@Test
    //void makeTags() throws SQLException {
    //    List<Tag> tagList = dbtest.getTags();
    //   if (tagList == null) {
    //       fail("The list of tags cannot be null");
    //   }
    //}
    @Disabled
    public void insertBookNullPointerException() throws SQLException, ClassNotFoundException {
        try {
            dbtest.insertBook(null, "");
            dbtest.insertBook(book1, null);
        } catch (NullPointerException e) {
            fail("No exception should be thrown");
        }
    }

    // issues with book.getId, ALWAYS call getBook(book) before referencing to its ID
    @Test
    public void insertRating() throws SQLException, ClassNotFoundException {
        Book book12 = dbtest.getBook("The Great Gatsby", "Francis Scott Fitzgerald");
        dbtest.insertRating(book12, 5);
        System.out.println(dbtest.getRating(book12));
        assertEquals(5, dbtest.getRating(book12));
    }

    //a response is shown in the console, but no error is thrown
    @Test
    public void insertRating2() throws SQLException, ClassNotFoundException {
        dbtest.insertRating(null, 5);
    }

    @Test
    public void insertBookshelfNullPointerException() throws SQLException, ClassNotFoundException {
        try {
           dbtest.insertBookshelf(null);
        } catch (NullPointerException e) {
            fail("No exception should be thrown");
        }
    }

    // does not really test anything
    @Test
    public void insertBookshelf() throws SQLException, ClassNotFoundException {
        dbtest.insertBookshelf(bookshelf1);
    }

    // not working
    @Test
    public void getBookshelfID() throws SQLException, ClassNotFoundException {
        dbtest.insertBookshelf(bookshelf2);
        int a = dbtest.getBookshelfID("bookshelf2");
        assertEquals(2, a);
    }

    @Test
    public void insertReview() throws SQLException, ClassNotFoundException {
            dbtest.insertReview(book1, "Made me cry");
    }

    @AfterAll
    public static void tearDown() throws SQLException, ClassNotFoundException {
        Book book12 = dbtest.getBook("The Great Gatsby", "Francis Scott Fitzgerald");
        Book book22 = dbtest.getBook("War and Peace", "A thicc big book");
        Book book32 = dbtest.getBook("The Lord of the Rings", "J. R. R. Tolkien");
        dbtest.deleteBook(book12);
        dbtest.deleteBook(book22);
        dbtest.deleteBook(book32);
    }
}