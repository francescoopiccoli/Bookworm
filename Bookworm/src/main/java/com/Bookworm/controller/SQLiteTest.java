package com.Bookworm.controller;


import com.Bookworm.model.Book;

import java.sql.*;

public class SQLiteTest {
    public static Connection con;
    private static boolean hasData = false;

    public ResultSet displayBooks() throws SQLException, ClassNotFoundException {
        if(con == null) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("select title from Book");
        return res;
    }

    private void getConnection() throws ClassNotFoundException, SQLException {
        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:BookwormDB.db");
        initialise();
    }

    public void addBook(String firstname, String lastname) throws ClassNotFoundException, SQLException {
        if(con == null) {
            // get connection
            getConnection();
        }
        PreparedStatement prep = con
                .prepareStatement("insert into Book values(?);");
        prep.setString(2, firstname);
        prep.setString(3, lastname);
        prep.execute();

    }


    private void initialise() throws SQLException {
        if( !hasData ) {
            hasData = true;
            // check for database table
            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Book';");
            if( !res.next()) {
                System.out.println("Building the Book table with prepopulated values.");
                // need to build the table
                Statement state2 = con.createStatement();
                state2.executeUpdate("create table Book(title varchar(60));");

                // inserting some sample data
                PreparedStatement prep = con.prepareStatement("insert into Book values(?);");
                prep.setString(1, "a");
                prep.execute();

            }

        }
    }

}
