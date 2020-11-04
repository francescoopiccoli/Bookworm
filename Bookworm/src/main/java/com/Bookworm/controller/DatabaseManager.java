package com.Bookworm.controller;


import com.Bookworm.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// sqlite.org/faq.html#q1
// https://www.tutorialspoint.com/sqlite/index.htm
// https://www.sqlitetutorial.net/sqlite-java/

//connection not closed, might cause lock errors
//review concept of close() statement and close() connection

public class DatabaseManager {
    public static Connection con;
    private static boolean hasData = false;
    private ResultSet res;

    private void getConnection() throws ClassNotFoundException, SQLException {
        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:BookwormDB.db");
        initialiseDB();
    }

    public void insertBook(Book b) throws ClassNotFoundException, SQLException {
        if(con == null) {
            // get connection
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("insert into Book values(?, ?, ?, ?, ?, ?, ?, ?, ?);");
        prep.setString(1, null);
        prep.setString(2, b.getName());
        prep.setString(3, b.getDescription());
        prep.setString(4, b.getAuthor());
        prep.setInt(5, b.getRating());
        prep.setString(6, b.getReview());
        // to be changed
        prep.setString(7, null);
        prep.setString(9, b.getImageURL());

        prep.execute();
        prep.close();
    }


    private void initialiseDB() throws SQLException {
        if( !hasData ) {
            hasData = true;
            // check for database table
            Statement state = con.createStatement();

            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Bookshelf';");
            if(!res.next()) {
                // need to build the table
                Statement state2 = con.createStatement();
                state2.executeUpdate("create table Bookshelf(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL)");
                state2.close();
            }

            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Book';");
            if(!res.next()) {
                // need to build the table
                Statement state1 = con.createStatement();
                state1.executeUpdate("create table Book(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "description," +
                        "author TEXT," +
                        "rating INTEGER," +
                        "review TEXT," +
                        "bookshelfID," +
                        "imageURL TEXT," +
                        "FOREIGN KEY(bookshelfID) REFERENCES Bookshelf(id))");
                state1.close();
            }

            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='BookTags';");
            if( !res.next()) {
                // need to build the table
                Statement state3 = con.createStatement();
                state3.executeUpdate("create table BookTags(" +
                        "bookID INTEGER," +
                        "tagName TEXT," +
                        "PRIMARY KEY (bookID, tagName)," +
                        "FOREIGN KEY(bookID) REFERENCES Book(id))");
                state3.close();
            }
        }
    }

    //return a single book (still as a ResultSet obj) corresponding to the given title/name
    public Book getBook(String name) throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select * from Book where name = '" + name + "';");
        return ModelBuilder.makeBook(res);
    }

    public List<Book> getBooks() throws SQLException, ClassNotFoundException {
        return ModelBuilder.makeBooks(getAll("Book"));
    }

    public List<String> getAuthors() throws SQLException, ClassNotFoundException {
        List<String> list = new LinkedList<>();
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select distinct author from Book order by author asc");
        while(res.next()) {
            list.add(res.getString(1));
        }
        return list;
    }


    //return all books (still as a ResultSet obj)  belonging to the specified bookshelf
    public ResultSet getBookShelf(String bookshelfID) throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select * from Book where bookshelfID = " + bookshelfID + ";");
        return res;
    }


    //return all the existing bookshelves (still as a ResultSet obj)
    public ResultSet getBookShelves() throws SQLException, ClassNotFoundException {
        return getAll("bookshelf");
    }


    public ResultSet getAll(String table) throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        PreparedStatement prep = con.prepareStatement("select * from "+table);
        res = prep.executeQuery();
        return res;
    }
    public ResultSet get(String table, int id) throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        PreparedStatement prep = con.prepareStatement("select * from ? where id = ?;");
        prep.setString(1, table);
        prep.setInt(2, id);

        res = prep.executeQuery();
        prep.close();
        return res;
    }
}
