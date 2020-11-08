package com.Bookworm.controller;

import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ModelBuilder {
    private static DatabaseManager databaseManager;

    public ModelBuilder() {
        databaseManager = DatabaseManager.getInstance();
    }

    public static Book makeBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setName(rs.getString("name"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));
        book.setRating(rs.getInt("rating"));
        book.setImageURL(rs.getString("imageURL"));
        book.setReview(rs.getString("review"));
        return book;
    }

    public static List<Book> makeBooks(ResultSet rs) throws SQLException {
        List<Book> list = new LinkedList<>();

        while(rs.next()) {
            Book book = makeBook(rs);
            list.add(book);
        }
        return list;
    }



    public static List<Bookshelf> makeBookshelves(ResultSet rs) throws SQLException {
        List<Bookshelf> list = new LinkedList<>();
        while(rs.next()) {
            Bookshelf bookshelf = new Bookshelf(rs.getString("name"), rs.getString("description")
            , null);
            //bookshelf.setBooks(); -> todo, but how?
            list.add(bookshelf);
        }
        return list;
    }

    public static List<Tag> makeTags(ResultSet rs) throws SQLException {
        List<Tag> list = new LinkedList<>();
        while(rs.next()) {
            Tag tag = new Tag(rs.getString("tagName"));
            //tag.setBook(...)
            list.add(tag);
        }
        return list;
    }
}
