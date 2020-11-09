package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface StorageManager {
    public void insertBook(Book book, String bookshelf) throws ClassNotFoundException, SQLException;
    public void insertBookshelf(Bookshelf bs)  throws ClassNotFoundException, SQLException;
    public boolean deleteBook(Book book) throws SQLException, ClassNotFoundException;
    public Bookshelf getBookShelf(String bookshelfID) throws SQLException, ClassNotFoundException ;
    public List<Bookshelf> getBookShelves() throws SQLException, ClassNotFoundException;
    public Book getBook(String name) throws SQLException, ClassNotFoundException;
    public List<Book> getBooks() throws SQLException, ClassNotFoundException;
    public ResultSet getAll(String table) throws SQLException, ClassNotFoundException;
    public List<String> getAuthors() throws SQLException, ClassNotFoundException;
}
