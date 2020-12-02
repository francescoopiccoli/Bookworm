package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
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
        assertNull(dbtest.getBook("does not exist", "Francis Scott Fitzgerald"));
    }

    @Test
    public void getBooks() throws SQLException, ClassNotFoundException {
        assertNotNull(dbtest.getBooks(), "List of book cannot be null ");
    }


    @Test
    public void getRewiew() throws SQLException, ClassNotFoundException {
        assertNull(dbtest.getReview(null));
        assertNotNull(dbtest.getReview(book1));
    }

    @Test
    public void getRating() throws SQLException, ClassNotFoundException {
        assertEquals(0, dbtest.getRating(book1));
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
        List<Bookshelf> list = dbtest.getBookShelves();
        if (list == null) {
            fail("Bookshelf List cannot be null");
        }
    }
    @Test
    public void getBookshelfID() throws SQLException, ClassNotFoundException {
     dbtest.insertBookshelf(bookshelf2);
     dbtest.getBookshelfID(bookshelf2.getName());
     assertNotNull(dbtest.getBookshelfID(bookshelf2.getName()));
    }
        @Test
    public void bookAlreadySaved() throws SQLException, ClassNotFoundException {
        assertTrue(dbtest.bookAlreadySaved(book1));
        assertFalse(dbtest.bookAlreadySaved(book4));
    }



    // issues with book.getId, ALWAYS call getBook(book) before referencing to its ID
    @Test
    public void insertRating() throws SQLException, ClassNotFoundException {
        Book book12 = dbtest.getBook("The Great Gatsby", "Francis Scott Fitzgerald");
        dbtest.insertRating(book12, 5);
        System.out.println(dbtest.getRating(book12));
        assertEquals(5, dbtest.getRating(book12));
        //a response is shown in the console, but no error is thrown
        dbtest.insertRating(null, 5);
    }

    @Test
    public void getBookshelfIdByBook() throws SQLException, ClassNotFoundException, NullPointerException {
        Book test = dbtest.getBook("The Great Gatsby", "Francis Scott Fitzgerald");
        assertNotNull(test);
        List<Book> list = new LinkedList<>();
        list.add(test);
        assertNotNull(list);
        assertEquals(-1,dbtest.getBookshelfIdByBook(test));

    }

    @Test
    public void getBookShelfBooks() throws SQLException, ClassNotFoundException, NullPointerException {
        List<Book> list = new LinkedList<>();
        list.add(book1);
        assertNotNull(dbtest.getBookShelves());
        dbtest.getBookshelfID(bookshelf1.getName());
        assertNotNull(dbtest.getBookShelfBooks(bookshelf1.getId()));
    }

    @Test
    public void insertBookshelf() throws SQLException, ClassNotFoundException, NullPointerException {
    dbtest.insertBookshelf(bookshelf1);
    List<Bookshelf> bookshelfList= dbtest.getBookShelves();
    for (int i=0; i<bookshelfList.size();i++){
        assertNotNull(bookshelfList.get(i));
    }

    }

    @Test
    public void deleteBookshelf() throws SQLException, ClassNotFoundException, NullPointerException {
        dbtest.deleteBookshelf(bookshelf1);
        dbtest.deleteBookshelf(bookshelf2);
        assertNotNull(dbtest.getBookShelves());
        if (dbtest.getBookShelves().isEmpty()){
           System.out.println("There are no more bookshelves");
        }
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