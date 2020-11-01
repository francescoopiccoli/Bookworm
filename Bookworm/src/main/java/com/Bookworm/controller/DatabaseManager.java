package com.Bookworm.controller;


import com.Bookworm.model.Book;

import java.sql.*;

// sqlite.org/faq.html#q1
// https://www.tutorialspoint.com/sqlite/index.htm
// https://www.sqlitetutorial.net/sqlite-java/

//connection not closed, might cause lock errors

public class DatabaseManager {
    public static Connection con;
    private static boolean hasData = false;

    public ResultSet displayBooks() throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("select name from Book");
        return res;
    }

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
        prep.setString(8, b.getCategory());
        prep.setString(9, b.getImageURL());

        prep.execute();
        prep.close();
    }


    private void initialiseDB() throws SQLException {
        if( !hasData ) {
            hasData = true;
            // check for database table
            Statement state = con.createStatement();

            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Bookshelf';");
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
                        "category TEXT," +
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

           /* //add foreign key on book table only when both book and bookshelf tables exist
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='BookTags';");
            if (res.next()) {
                res =  state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Book';");
                if(res.next()){
                    Statement state4 = con.createStatement();
                    state4.executeUpdate("ALTER TABLE Book ADD CONSTRAINT bo" +
                            "                  FOREIGN KEY (bookshelfID)" +
                            "                  REFERENCES Bookshelf(id);");
                }
            }

            //add foreign key on tag table only when both book and booktag tables exist
            res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Book';");
            if (res.next()) {
                res =  state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Book';");
                if(res.next()){
                    Statement state4 = con.createStatement();
                    state4.executeUpdate("ALTER TABLE Book ADD CONSTRAINT bo" +
                            "                  FOREIGN KEY (bookshelfID)" +
                            "                  REFERENCES Bookshelf(id);");
                }
            }*/
        }
    }
}
