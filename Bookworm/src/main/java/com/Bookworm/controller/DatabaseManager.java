package com.Bookworm.controller;


import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//QUESTIONS:
// if I delete a bookshelf, what happens to the book within it?
// inserting a book in default reading list, what about is bookshelfID attribute? as for now, set to 0

public class DatabaseManager implements BookStorage {
    public static Connection con;
    private Statement s;
    private PreparedStatement ps;
    private ResultSet res;
    private static DatabaseManager dbmanager;
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());


    //singleton design pattern
    static {
        try {
            dbmanager = new DatabaseManager();
        } catch (SQLException | ClassNotFoundException throwables) {
            LOGGER.log( Level.SEVERE, throwables.toString(), throwables);
            //throwables.printStackTrace();
        }
    }

    //initialise DB in the constructor
    private DatabaseManager() throws SQLException, ClassNotFoundException {
        initialiseDB();
    }

    public static DatabaseManager getInstance() {
        return dbmanager;
    }

    //connecting to the local database file "BookwormDB.db"
    private void getConnection() throws ClassNotFoundException, SQLException {
        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, if it's new database, it will be created in the project folder
        con = DriverManager.getConnection("jdbc:sqlite:BookwormDB.db");
    }

    //initialiseDB creates the database tables in case they do not exist yet
    private void initialiseDB() throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            //sqlite Foreign key constraints are not active by default. You must enable them with the following command, once per database session :
            //PRAGMA foreign_keys = ON;
            //source: https://stackoverflow.com/questions/52226080/sqlite-on-delete-cascade-is-not-working-right
            s = con.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON;");

        } finally {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        }

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            s = con.createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Bookshelf(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT)");
        } finally {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        }

        try {
            if (con == null || con.isClosed()) {
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
                    "UNIQUE(name,author)," +
                    "FOREIGN KEY(bookshelfID) REFERENCES Bookshelf(id) ON DELETE CASCADE)"); //!!! ON DELETE CASCADE not sure if works
        } finally {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        }


        //if no default bookself is present it is created
    }

    // retrieve a book from database given the name and the author of the book
    public Book getBook(String name, String author) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT * FROM Book WHERE name = ? and author = ?;");
            ps.setString(1, name);
            ps.setString(2, author);
            res = ps.executeQuery();

            //check if resultset returned something
            if (!res.isClosed()) {
                Book book = new Book();
                book.setId(res.getInt("id"));
                book.setName(res.getString("name"));
                book.setAuthor(res.getString("author"));
                book.setDescription(res.getString("description"));
                book.setRating(res.getInt("rating"));
                book.setImageURL(res.getString("imageURL"));
                book.setReview(res.getString("review"));
                return book;
            } else {
                return null;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    //retrieves all books saved in the database
    public List<Book> getBooks() throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT * FROM Book");
            res = ps.executeQuery();
            List<Book> list = new LinkedList<>();

            while (res.next()) {
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

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    //retrieves the review of the specified book
    public String getReview(Book b) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT review FROM Book WHERE id = ?;");
            ps.setInt(1, b.getId());
            res = ps.executeQuery();
            //res.isClosed == nothing found
            if (res.isClosed()) {
                return "";
            }
            return res.getString("review");
        } catch (NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in getReview: book was null", e);
            return null;
        }
        finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public int getBookshelfIdByBook(Book b) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT bookshelfID FROM Book WHERE id = ?;");
            ps.setInt(1, b.getId());
            res = ps.executeQuery();
            //res.isClosed == nothing found
            if (res.isClosed()) {
                return -1;
            }
            if(res.getBoolean(1)) // not null
                return res.getInt(1);
            else
                return -1;
        } catch(NullPointerException e) {
            return -1;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    //retrieves the rating of the specified book
    public int getRating(Book b) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT rating FROM Book WHERE id = ?;");
            ps.setInt(1, b.getId());
            res = ps.executeQuery();
            //res.isClosed == nothing found
            if (res.isClosed()) {
                return 0;
            }
            return res.getInt("rating");
        } catch(NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in getRating: book is null", e);
            //System.out.println("Error in getRating: book is null");
            return -1;
        }
        finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    //retrieves a list of strings containing all authors
    public List<String> getAuthors() throws SQLException, ClassNotFoundException {
        try {
            List<String> list = new LinkedList<>();
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT distinct author FROM Book order by author asc");
            res = ps.executeQuery();
            while (res.next()) {
                list.add(res.getString(1));
            }
            return list;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    //return all books belonging to a bookshelf
    //could be refactored and integrated directly in getBooks by passing a bookshelfID as parameter
    public List<Book> getBookShelfBooks(int bookshelfID) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }

            ps = con.prepareStatement("SELECT * FROM Book WHERE bookshelfID = ? ;");
            ps.setInt(1, bookshelfID);
            res = ps.executeQuery();

            List<Book> list = new LinkedList<>();
            while (res.next()) {
                Book book = new Book();
                book.setId(res.getInt("id"));
                book.setName(res.getString("name"));
                book.setAuthor(res.getString("author"));
                book.setDescription(res.getString("description"));
                book.setRating(res.getInt("rating"));
                book.setImageURL(res.getString("imageURL"));
                book.setReview(res.getString("review"));
                list.add(book);
                //bookshelf.setBooks(); -> todo, but how?
            }
            return list;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    //return a list of bookshelf objects, namely all the bookshelves existing in the database
    public List<Bookshelf> getBookShelves() throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT * FROM Bookshelf");
            res = ps.executeQuery();
            List<Bookshelf> list = new LinkedList<>();
            while (res.next()) {
                Bookshelf bookshelf = new Bookshelf();
                bookshelf.setId(res.getInt("id"));
                bookshelf.setName(res.getString("name"));
                bookshelf.setDescription(res.getString("description"));
                //bookshelf.setBooks(); -> todo, but how?
                list.add(bookshelf);
            }
            return list;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean bookAlreadySaved(Book book) throws SQLException, ClassNotFoundException {
        return (getBook(book.getName(), book.getAuthor()) != null);
    }

    //since bookshelf is not an attribute of Book, it has to be passed as parameter
    public void insertBook(Book b, String bookshelf) throws ClassNotFoundException, SQLException {
        try {
            int bookshelfID = getBookshelfID(bookshelf);
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, null);
            ps.setString(2, b.getName());
            ps.setString(3, b.getDescription());
            ps.setString(4, b.getAuthor());
            ps.setInt(5, b.getRating());
            ps.setString(6, b.getReview());
            ps.setString(7, null); // bookshelf.getId
            // in case bookshelf id is not found, the bookshelfID column in DB will contain a 0
            if (bookshelfID == 0) {
                ps.setInt(7, 0);
            }
            ps.setInt(7, bookshelfID);
            ps.setString(8, b.getImageURL());
            ps.executeUpdate();
        } catch (NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in insert book: inserted bookshelf or book was null", e);
            //System.out.println("Error in insert book: inserted bookshelf or book was null");
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    //instead of the whole book, just the id could be passed as parameter
    public void insertRating(Book b, int rating) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("UPDATE Book set rating = ? WHERE id= ?");
            ps.setInt(1, rating);
            ps.setInt(2, b.getId());
            ps.executeUpdate();

        } catch(NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in insertRating: book is empty", e);
            //System.out.println("Error in insertRating: book is empty");
        }
        finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    //instead of the whole book, just the id could be passed as parameter
    public void insertReview(Book b, String review) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("UPDATE Book set review = ? WHERE id= ?");
            ps.setString(1, review);
            ps.setInt(2, b.getId());
            ps.executeUpdate();

        } catch(NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in insert review, book was null", e);
            //System.out.println("Error in insert review, book was null");
        }
        finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    public void insertBookshelf(Bookshelf bs) throws ClassNotFoundException, SQLException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("INSERT INTO Bookshelf VALUES (?, ?, ?);");
            ps.setString(1, null);
            ps.setString(2, bs.getName());
            ps.setString(3, bs.getDescription());
            ps.executeUpdate();
        } catch (NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in insert bookshelf: inserted bookshelf was null", e);
            //System.out.println("Error in insert bookshelf: inserted bookshelf was null");
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    public int getBookshelfID(String bookshelfName) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("SELECT id FROM Bookshelf WHERE name = ?;");
            ps.setString(1, bookshelfName);
            res = ps.executeQuery();
            //res.isClosed == nothing found
            if (res.isClosed()) {
                return -1;
            }
            return res.getInt("id");
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (res != null) {
                res.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    public boolean deleteBook(Book book) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            if (book == null) {
                System.out.println("Error in delete book: book is null");
                return false;
            }
            ps = con.prepareStatement("DELETE FROM Book WHERE id = ?");
            ps.setInt(1, book.getId());
            System.out.println(book.getId());
            return ps.execute();

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }


    public boolean deleteBookshelf(Bookshelf bookshelf) throws SQLException, ClassNotFoundException {
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            ps = con.prepareStatement("DELETE FROM Bookshelf WHERE id = ?");
            ps.setInt(1, bookshelf.getId());
            return ps.execute();

        } catch (NullPointerException e) {
            LOGGER.log( Level.SEVERE, "Error in delete bookshelf: bookshelf is null", e);
            //System.out.println("Error in delete bookshelf: bookshelf is null");
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}


