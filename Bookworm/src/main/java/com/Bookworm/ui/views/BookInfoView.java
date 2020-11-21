package com.Bookworm.ui.views;


import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.ui.widgets.BookListWidget;
import com.Bookworm.ui.widgets.BookWidget;
import com.Bookworm.ui.widgets.StarWidget;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.*;

public class BookInfoView extends BorderPane {


    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 350;
    private static final String LABEL_NO_BOOKSHELF = "[No Bookshelf]";
    private static final String LABEL_DEFAULT_BOOKSHELF = "Default";
    private static final int ID_NO_BOOKSHELF = -5;
    private static final int ID_DEFAULT_BOOKSHELF = -6;

    public static ArrayList<Bookshelf> bookShelf = new ArrayList<>();
    public static List<Book> bookList = new LinkedList<>(); // ?
    private  List<BookWidget> books;
    private Book book;
    private BookListWidget parent;
    ImageView imageView;
    public static final DatabaseManager dbManager = DatabaseManager.getInstance(); // just 1 instance per app! (pass from app?)

    public static List<Book> getBookList() {
        return bookList;
    }

    public static void setBookList(List<Book> bookList) {
        BookInfoView.bookList = bookList;
    }

    public List<BookWidget> getBooks() {
        return books;
    }

    public void setBooks(List<BookWidget> books) {
        this.books = books;
    }

    public static ArrayList<Bookshelf> getBookShelf() {
        return bookShelf;
    }

    public static void setBookShelf(ArrayList<Bookshelf> bookShelf) {
        BookInfoView.bookShelf = bookShelf;
    }


    public BookInfoView(Book book, BookListWidget parent) throws SQLException, ClassNotFoundException {
        this.book = book;
        this.parent = parent;
        Image image;
        try {
           image = new Image(book.getImageURL(), true);
        } catch (Exception e) {
            image = new Image(getClass().getResourceAsStream(BookWidget.PLACEHOLDER_IMAGE_URI));
        }
        this.imageView = new ImageView(image);

        setTop(addHBoxTop());
        setCenter(addVBox());

    }

    public static void spawnWindow(Book book, BookListWidget parent) throws SQLException, ClassNotFoundException {
        spawnWindow(book, DEFAULT_WIDTH, parent);
    }

    public static void spawnWindow(Book book, Image image, BookListWidget parent) throws SQLException, ClassNotFoundException {
        spawnWindow(book, DEFAULT_WIDTH, image, parent);
    }

    public static void spawnWindow(Book book, int w, BookListWidget parent) throws SQLException, ClassNotFoundException {
        Image image = new Image(book.getImageURL());
        spawnWindow(book, w, image, parent);
    }

    public static void spawnWindow(Book book, int w, Image image, BookListWidget parent) throws SQLException, ClassNotFoundException {
        ImageView imageView = new ImageView(image);
        BookInfoView bookInfoView = new BookInfoView(book, parent);

        Stage stage = new Stage();
        stage.setTitle(book.getName());
        Scene scene = new Scene(bookInfoView);
        stage.setScene(scene);
        stage.setWidth(w);
        stage.show();
    }

    public HBox addHBoxTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));

        //book title

        Label displayTitle = new Label();
        displayTitle.setText(book.getName());
        displayTitle.getStyleClass().add("display");
        displayTitle.setFont(Font.font("Cantarell", FontWeight.BOLD, 18));


        //book author

        Label displayAuthor = new Label();
        displayAuthor.setText(book.getAuthor());
        displayAuthor.getStyleClass().add("display");
        displayAuthor.setFont(Font.font("Arial", FontWeight.BOLD, 12));


        //bookshelf
        BorderPane bookshelfBox = new BorderPane();
        ComboBox<Bookshelf> comboBookshelf = new ComboBox<>();
        comboBookshelf.setPlaceholder(new Label("None"));
        //list stays empty need to check later

        Bookshelf noBookshelf = new Bookshelf(LABEL_NO_BOOKSHELF, "", null);
        noBookshelf.setId(ID_NO_BOOKSHELF);
        Bookshelf defaultBookshelf = new Bookshelf(LABEL_DEFAULT_BOOKSHELF, "The default reading list", null);
        defaultBookshelf.setId(ID_DEFAULT_BOOKSHELF);

        ObservableList<Bookshelf> list = FXCollections.observableArrayList(bookShelf);
        list.add(0, noBookshelf);
        list.add(1, defaultBookshelf);

        try {
            LinkedList<Bookshelf> b = (LinkedList<Bookshelf>) DatabaseManager.getInstance().getBookShelves();
            for(Bookshelf bookshelf : b) {
                list.add(bookshelf.getId(), bookshelf);
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        comboBookshelf.setItems(list);
        comboBookshelf.getSelectionModel().select(0);
        comboBookshelf.setConverter(new StringConverter<>() {

            @Override
            public String toString(Bookshelf bookshelf) {
                if (bookshelf != null)
                    return bookshelf.getName();
                else
                    return null;
            }

            @Override
            public Bookshelf fromString(String s) {
                return null;
            }
        });

        try {
            if(dbManager.bookAlreadySaved(book)) {
                // needs getBookshelfByBook(Book book)!
                comboBookshelf.setValue(defaultBookshelf);
            } else {
                comboBookshelf.setValue(noBookshelf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // saving book: problem -> bookId is never assigned
        comboBookshelf.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(!oldVal.getName().equals(newVal.getName())) {
                // delete first
                try {
                    dbManager.deleteBook(book);
                    if(parent != null) {
                        List<BookWidget> books = parent.getBooks();

                        books.removeIf(widget -> widget.getBook().getId() == book.getId());
                        parent.updateList(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // then reinsert if needed

                if(newVal.getId() != ID_NO_BOOKSHELF) {
                    try {
                        dbManager.insertBook(book, newVal.getName());
                        updateBookId();
                        if (parent != null) {
                            List<BookWidget> books = parent.getBooks();
                            books.add(new BookWidget(book));
                            parent.updateList(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });




        bookshelfBox.setRight(comboBookshelf);

        //Vbox for left elements
        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.getChildren().addAll(displayTitle, displayAuthor);
        bookshelfBox.setLeft(vBox);

        HBox.setHgrow(bookshelfBox, Priority.ALWAYS);
        hbox.getChildren().addAll(bookshelfBox);

        return hbox;
    }

    private void updateBookId() throws SQLException, ClassNotFoundException {
        int newId = dbManager.getBook(book.getName(), book.getAuthor()).getId();
        book.setId(newId);
    }

    public VBox addVBox() throws SQLException, ClassNotFoundException {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);


        //description

        Label description = new Label("Short Description");
        if(book.getDescription() != null)
            description.setText(book.getDescription());
        description.setWrapText(true);
        description.setFont(Font.font(null, FontWeight.NORMAL, 12));
        description.getStyleClass().add("description");
        description.setPadding(new Insets(10));
        HBox.setHgrow(description, Priority.ALWAYS);

        //scroll for description textarea

        ScrollPane scrollPane = new ScrollPane(description);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-description");


        //Review and rating Area
        TextArea review = new TextArea("review");
        //in case the book is already in DB, retreives the already existing review
        if(dbManager.bookAlreadySaved(book)) {
            review.setEditable(true);
            review.setText(dbManager.getReview(book));
            // override remote rating with local rating if it exists
            book.setRating(dbManager.getRating(book));
            book.setReview(dbManager.getReview(book));
        } else {
            review.setText("Add the book to your library to review it!");
        }
        //generates starwidget calling the static method getStarWidget of Starwidget class
        HBox starwidget = StarWidget.getStarWidget(this, book);

        //if review is changed, update/insert the new review in the database
        review.textProperty().addListener((observable, oldValue, newValue) -> {
            // this will run whenever text is changed
            try {
                if(dbManager.bookAlreadySaved(book)) {
                    updateBookId();
                    dbManager.insertReview(book, review.getText());
                } else {
                    System.out.println("Book not saved, review has been ignored");
                    Platform.runLater(() -> review.setText("Add the book to your library to review it!"));
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
        review.setFont(Font.font(null, FontWeight.NORMAL, 12));
        review.setWrapText(true);
        review.setPrefColumnCount(15);


        //scroll for review textarea
        ScrollPane scrollPane2 = new ScrollPane(review);
        scrollPane2.getStyleClass().add("scrollReview");
        scrollPane2.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);

        //Vbox for review
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(10);
        reviewBox.getChildren().addAll(starwidget, scrollPane2);

        //left side
        VBox left = new VBox();
        left.setSpacing(20);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane2, Priority.ALWAYS);
        left.getChildren().addAll(scrollPane);

        //image
        imageView.setFitWidth(200);
        imageView.setFitHeight(280);
        imageView.setPreserveRatio(false);
        HBox.setHgrow(imageView, Priority.ALWAYS);
        VBox.setVgrow(imageView, Priority.ALWAYS);

        //overall view
        HBox overall = new HBox();
        VBox.setVgrow(overall, Priority.NEVER);

        overall.setSpacing(20);
        overall.maxHeightProperty().bind(imageView.fitHeightProperty());
        HBox.setHgrow(reviewBox, Priority.NEVER);
        overall.getChildren().addAll(imageView, left, new Separator(Orientation.VERTICAL), reviewBox);



        vbox.getChildren().addAll(overall);


        return vbox;
    }

//method called in StarWidget class to refresh the bookinfo window once a new rating has been inserted
    public void refresh() throws SQLException, ClassNotFoundException {
        setCenter(addVBox());
    }
}





