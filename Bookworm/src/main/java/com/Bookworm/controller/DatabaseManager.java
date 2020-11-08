package com.Bookworm.controller;


import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// sqlite.org/faq.html#q1
// https://www.tutorialspoint.com/sqlite/index.htm
// https://www.sqlitetutorial.net/sqlite-java/

//connection not closed, might cause lock errors
//review concept of close() statement and close() connection

public class DatabaseManager {
    public static Connection con;
    private ResultSet res;
    private static DatabaseManager dbmanager;

    static {
        try {
            dbmanager = new DatabaseManager();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DatabaseManager() throws SQLException, ClassNotFoundException {
        initialiseDB();
    }

    public static DatabaseManager getInstance() {
        return dbmanager;
    }

    private void getConnection() throws ClassNotFoundException, SQLException {
        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:BookwormDB.db");
    }



    private void initialiseDB() throws SQLException, ClassNotFoundException {

        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }

        Statement state1 = con.createStatement();
        state1.executeUpdate("CREATE TABLE IF NOT EXISTS Bookshelf(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT)");
        state1.close();

        Statement state2 = con.createStatement();
        state2.executeUpdate("CREATE TABLE IF NOT EXISTS Book(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description," +
                "author TEXT," +
                "rating INTEGER," +
                "review TEXT," +
                "bookshelfID," +
                "imageURL TEXT," +
                "FOREIGN KEY(bookshelfID) REFERENCES Bookshelf(id))");
        state2.close();

        Statement state3 = con.createStatement();
        state3.executeUpdate("CREATE TABLE IF NOT EXISTS BookTags(" +
                "bookID INTEGER," +
                "tagName TEXT," +
                "PRIMARY KEY (bookID, tagName)," +
                "FOREIGN KEY(bookID) REFERENCES Book(id))");
        state3.close();
        con.close();
    }


    public Book getBook(String name) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select * from Book where name = '" + name + "';");
        state.close();
        con.close();
        return ModelBuilder.makeBook(res);
    }

    public List<Book> getBooks() throws SQLException, ClassNotFoundException {
        return ModelBuilder.makeBooks(getAll("Book"));
    }

    public List<String> getAuthors() throws SQLException, ClassNotFoundException {
        List<String> list = new LinkedList<>();
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select distinct author from Book order by author asc");
        while(res.next()) {
            list.add(res.getString(1));
        }
        state.close();
        con.close();
        return list;
    }


    //return all books (still as a ResultSet obj)  belonging to the specified bookshelf
    public Bookshelf getBookShelf(String bookshelfID) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }
        Statement state = con.createStatement();
        res = state.executeQuery("select * from Book where bookshelfID = " + bookshelfID + ";");
        state.close();
        con.close();
        return ModelBuilder.makeBookshelf(res);
    }


    //return all the existing bookshelves (still as a ResultSet obj)
    public List<Bookshelf> getBookShelves() throws SQLException, ClassNotFoundException {
        return ModelBuilder.makeBookshelves(getAll("Bookshelf"));
    }


    public ResultSet getAll(String table) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }
        PreparedStatement prep = con.prepareStatement("select * from "+table);
        res = prep.executeQuery();
        prep.close();
        con.close();
        return res;
    }


    public ResultSet get(String table, int id) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }
        PreparedStatement prep = con.prepareStatement("select * from ? where id = ?;");
        prep.setString(1, table);
        prep.setInt(2, id);

        res = prep.executeQuery();
        prep.close();
        con.close();
        return res;
    }

    public String insert(String table) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }

        return "insert into "  + table + " VALUES";
    }


    //since bookshelf is not an attribute of Book, it has to be passed as parameter
    public void insertBook(Book b, String bookshelf) throws ClassNotFoundException, SQLException {
        int bookshelfID = getBookshelfID(bookshelf);
        String query = insert("Book");
        PreparedStatement prep = con.prepareStatement(query + " (?, ?, ?, ?, ?, ?, ?, ?, ?);");
        prep.setString(1, null);
        prep.setString(2, b.getName());
        prep.setString(3, b.getDescription());
        prep.setString(4, b.getAuthor());
        prep.setInt(5, b.getRating());
        prep.setString(6, b.getReview());
        prep.setString(7, null); // bookshelf.getId

        // in case bookshelf id is not found, the bookshelfID column in DB will contain a 0
        if(bookshelfID == 0)
            prep.setInt(7, 0);
        else
            prep.setInt(7, bookshelfID);


        prep.setString(8, b.getImageURL());
        prep.execute();
        prep.close();
        con.close();
    }


    public void insertBookshelf(Bookshelf bs) throws ClassNotFoundException, SQLException {

        String query = insert("Bookshelf");
        PreparedStatement prep = con.prepareStatement(query + " (?, ?, ?);");
        prep.setString(1, null);
        prep.setString(2, bs.getName());
        prep.setString(3, bs.getDescription());
        prep.execute();
        prep.close();
        con.close();
    }


    private int getBookshelfID(String bookshelfName) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("select id from Bookshelf where name = ?;");
        prep.setString(1, bookshelfName);
        res = prep.executeQuery();
        prep.close();
        //res.isClosed == nothing found
        con.close();
        if(res.isClosed()) {
        return 0;
        }
        return res.getInt("id");
    }


    public boolean delete(String table, int id) throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            // get connection
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("delete from "+table+" where id = ?");
        prep.setInt(1, id);

        boolean result = prep.execute();
        prep.close();
        con.close();
        return result;
    }


    public boolean deleteBook(Book book) throws SQLException, ClassNotFoundException {
        return delete("Book", book.getId());
    }
}
