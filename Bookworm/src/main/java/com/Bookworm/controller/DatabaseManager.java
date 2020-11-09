package com.Bookworm.controller;


import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

// sqlite.org/faq.html#q1
// https://www.tutorialspoint.com/sqlite/index.htm
// https://www.sqlitetutorial.net/sqlite-java/

public class DatabaseManager implements StorageManager {
    public static Connection con;
    private Statement s;
    private PreparedStatement ps;
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

        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Bookshelf(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT)");
        } finally {
            s.close();
            con.close();
        }
        try{
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Book(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description," +
                    "author TEXT," +
                    "rating INTEGER," +
                    "review TEXT," +
                    "bookshelfID," +
                    "imageURL TEXT," +
                    "FOREIGN KEY(bookshelfID) REFERENCES Bookshelf(id))");
        }
        finally{
            s.close();
            con.close();
        }

        try{
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS BookTags(" +
                    "bookID INTEGER," +
                    "tagName TEXT," +
                    "PRIMARY KEY (bookID, tagName)," +
                    "FOREIGN KEY(bookID) REFERENCES Book(id))");
        }
        finally {
            s.close();
            con.close();
        }
    }


    public Book getBook(String name) throws SQLException, ClassNotFoundException {
        try{
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            res = s.executeQuery("select * from Book where name = '" + name + "';");
            Book book = new Book();
            book.setId(res.getInt("id"));
            book.setName(res.getString("name"));
            book.setAuthor(res.getString("author"));
            book.setDescription(res.getString("description"));
            book.setRating(res.getInt("rating"));
            book.setImageURL(res.getString("imageURL"));
            book.setReview(res.getString("review"));
            return book;
        } finally{
            s.close();
            res.close();
            con.close();
        }

    }

    public List<Book> getBooks() throws SQLException, ClassNotFoundException {

        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            ps = con.prepareStatement("select * from Book");
            res = ps.executeQuery();
            List<Book> list = new LinkedList<>();

            while(res.next()) {
                Book book = new Book();
                book.setId(res.getInt("id"));
                book.setName(res.getString("name"));
                book.setAuthor(res.getString("author"));
                book.setDescription(res.getString("description"));
                book.setRating(res.getInt("rating"));
                book.setImageURL(res.getString("imageURL"));
                book.setReview(res.getString("review"));
                list.add(book);
            }

            return list;

        } finally{
            if(ps != null) {
                ps.close();
            }
            res.close();
            con.close();
        }
    }

    public List<String> getAuthors() throws SQLException, ClassNotFoundException {

        try{
            List<String> list = new LinkedList<>();
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            res = s.executeQuery("select distinct author from Book order by author asc");
            while(res.next()) {
                list.add(res.getString(1));
            }
            return list;
        } finally {
            s.close();
            res.close();
            con.close();
        }
    }

    //return all books belonging to a bookshelf
// !!! error !!! query retrives books, mapping instead configure as a bookshelf
    public Bookshelf getBookShelf(String bookshelfID) throws SQLException, ClassNotFoundException {

        try{
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            s = con.createStatement();
            res = s.executeQuery("select * from Book where bookshelfID = " + bookshelfID + ";");
            Bookshelf bookshelf = new Bookshelf();
            bookshelf.setId(res.getInt("id"));
            bookshelf.setName(res.getString("name"));
            bookshelf.setDescription(res.getString("description"));
            return bookshelf;
        } finally {
            s.close();
            res.close();
            con.close();
        }
    }


    //return all bookshelf objects, with name and descriptions
    public List<Bookshelf> getBookShelves() throws SQLException, ClassNotFoundException {

        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            ps = con.prepareStatement("select * from Bookshelf");
            res = ps.executeQuery();
            List<Bookshelf> list = new LinkedList<>();
            while(res.next()) {
                Bookshelf bookshelf = new Bookshelf(res.getString("name"), res.getString("description")
                        , null);
                //bookshelf.setBooks(); -> todo, but how?
                list.add(bookshelf);
            }
            return list;
        } finally{
            if(ps != null) {
                ps.close();
            }
            res.close();
            con.close();
        }
    }

/*
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
    }*/


    //since bookshelf is not an attribute of Book, it has to be passed as parameter
    public void insertBook(Book b, String bookshelf) throws ClassNotFoundException, SQLException {
        try {
            int bookshelfID = getBookshelfID(bookshelf);

            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }

            ps = con.prepareStatement("insert into Book VALUES  (?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, null);
            ps.setString(2, b.getName());
            ps.setString(3, b.getDescription());
            ps.setString(4, b.getAuthor());
            ps.setInt(5, b.getRating());
            ps.setString(6, b.getReview());
            ps.setString(7, null); // bookshelf.getId

            // in case bookshelf id is not found, the bookshelfID column in DB will contain a 0
            if (bookshelfID == 0)
                ps.setInt(7, 0);
            else
                ps.setInt(7, bookshelfID);


            ps.setString(8, b.getImageURL());
            ps.executeUpdate();
        } finally {
            if(ps != null) {
                ps.close();
            }
            con.close();
        }

    }


    public void insertBookshelf(Bookshelf bs) throws ClassNotFoundException, SQLException {
        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            ps = con.prepareStatement("insert into Bookshelf VALUES (?, ?, ?);");
            ps.setString(1, null);
            ps.setString(2, bs.getName());
            ps.setString(3, bs.getDescription());
            ps.executeUpdate();

        } finally {
            if(ps != null) {
                ps.close();
            }
            con.close();
        }
    }


    public int getBookshelfID(String bookshelfName) throws SQLException, ClassNotFoundException {
        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            ps = con.prepareStatement("select id from Bookshelf where name = ?;");
            ps.setString(1, bookshelfName);
            res = ps.executeQuery();
            //res.isClosed == nothing found
            if (res.isClosed()) {
                return 0;
            }
            return res.getInt("id");
        } finally {
            if(ps != null) {
                ps.close();
            }
            res.close();
            con.close();
        }
    }


    public boolean delete(String table, int id) throws SQLException, ClassNotFoundException {
        try {
            if(con == null || con.isClosed()) {
                // get connection
                getConnection();
            }
            ps = con.prepareStatement("delete from "+table+" where id = ?");
            ps.setInt(1, id);
            boolean result = ps.execute();
            return result;
        } finally {
            if(ps != null) {
                ps.close();
            }
            con.close();
        }


    }

    public boolean deleteBook(Book book) throws SQLException, ClassNotFoundException {
        return delete("Book", book.getId());
    }
}
