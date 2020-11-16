package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.sql.SQLException;
import java.util.List;

public interface BookStorage {
    void insertBook(Book book, String bookshelf) throws ClassNotFoundException, SQLException;
    void insertBookshelf(Bookshelf bs)  throws ClassNotFoundException, SQLException;
    boolean deleteBook(Book book) throws SQLException, ClassNotFoundException;
    List<Book> getBookShelfBooks(int bookshelfID) throws SQLException, ClassNotFoundException ;
    List<Bookshelf> getBookShelves() throws SQLException, ClassNotFoundException;
    Book getBook(String name, String author) throws SQLException, ClassNotFoundException;
    List<Book> getBooks() throws SQLException, ClassNotFoundException;
    List<String> getAuthors() throws SQLException, ClassNotFoundException;
    void insertRating(Book b, int rating) throws SQLException, ClassNotFoundException;
    void insertReview(Book b, String review) throws SQLException, ClassNotFoundException;
    int getRating(Book b) throws SQLException, ClassNotFoundException;
    String getReview(Book b)  throws SQLException, ClassNotFoundException;
    boolean bookAlreadySaved(Book book) throws SQLException, ClassNotFoundException;
}
