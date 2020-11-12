package com.Bookworm.ui.views;


import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.model.Tag;
import com.Bookworm.ui.widgets.BookListWidget;
import com.Bookworm.ui.widgets.BookWidget;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
    private static final String LABEL_NO_BOOKSHELF = "Save to bookshelf...";
    private static final String LABEL_DEFAULT_BOOKSHELF = "Default";

    ArrayList<Tag> tags;
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


    public BookInfoView(Book book, BookListWidget parent) {
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

    public static void spawnWindow(Book book, BookListWidget parent) {
        spawnWindow(book, DEFAULT_WIDTH, DEFAULT_HEIGHT, parent);
    }

    public static void spawnWindow(Book book, Image image, BookListWidget parent) {
        spawnWindow(book, DEFAULT_WIDTH, DEFAULT_HEIGHT, image, parent);
    }

    public static void spawnWindow(Book book, int w, int h, BookListWidget parent) {
        Image image = new Image(book.getImageURL());
        spawnWindow(book, w, h, image, parent);
    }

    public static void spawnWindow(Book book, int w, int h, Image image, BookListWidget parent) {
        ImageView imageView = new ImageView(image);
        BookInfoView bookInfoView = new BookInfoView(book, parent);

        Stage stage = new Stage();
        stage.setTitle(book.getName());
        Scene scene = new Scene(bookInfoView, w, h);
        stage.setScene(scene);
        stage.show();
    }

    public HBox addHBoxTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(40);

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
        HBox bookshelfBox = new HBox();
        ComboBox bookshelfCombo = new ComboBox();
        HBox.setHgrow(bookshelfCombo, Priority.ALWAYS);
        ComboBox<Bookshelf> comboBookshelf = new ComboBox<Bookshelf>();
        comboBookshelf.setPlaceholder(new Label("None"));
      //  System.out.println(bookList.toString());
      //  System.out.println(bookShelf.toString());
        //list stays empty need to check later

        ObservableList<Bookshelf> list = FXCollections.observableArrayList(bookShelf);
        list.add(0, new Bookshelf(LABEL_NO_BOOKSHELF, "", null));
        list.add(1, new Bookshelf(LABEL_DEFAULT_BOOKSHELF, "The default reading list", null));

        try {
            LinkedList<Bookshelf> b = (LinkedList<Bookshelf>) DatabaseManager.getInstance().getBookShelves();
            for(Bookshelf bookshelf : b) {
                list.add(bookshelf.getId(), bookshelf);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        comboBookshelf.setItems(list);
        comboBookshelf.getSelectionModel().select(0);
        comboBookshelf.setConverter(new StringConverter<Bookshelf>() {

            @Override
            public String toString(Bookshelf bookshelf) {
                if(bookshelf != null)
                    return bookshelf.getName();
                else
                    return null;
            }

            @Override
            public Bookshelf fromString(String s) {
                return null;
            }
        });

        comboBookshelf.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(!oldVal.getName().equals(newVal.getName())) {
                System.out.println(oldVal+" "+newVal);
                if(oldVal.getName().equals(LABEL_NO_BOOKSHELF)) {
                    // todo: insert book only if it really exists
                    try {
                        if(dbManager.getBook(book.getName(), book.getAuthor()) == null){
                            dbManager.insertBook(book, null);

                            if(parent != null) {
                                List<BookWidget> books = parent.getBooks();
                                books.add(new BookWidget(book));
                                parent.updateList(null);
                            }
                            System.out.println("Saved book "+book.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(newVal.getName().equals(LABEL_NO_BOOKSHELF)) {
                    try {
                        if(dbManager.getBook(book.getName(), book.getAuthor()) != null){
                            dbManager.deleteBook(book);
                            if(parent != null) {
                                List<BookWidget> books = parent.getBooks();
                                Iterator<BookWidget> iter = books.iterator();

                                while (iter.hasNext()) {
                                    BookWidget widget = iter.next();

                                    if (widget.getBook().getId() == book.getId()) {
                                       iter.remove();
                                    }
                                }
                                parent.updateList(null);
                            }
                            System.out.println("Deleted book "+book.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //if(newVal.equals(LABEL_DEFAULT_BOOKSHELF))
                        // setBookshelf(book, getIdFromName(name))
                    //else
                        // setBookshelf(book, null)
                }
            }
        });




        bookshelfBox.setSpacing(20);
        bookshelfBox.getChildren().addAll(comboBookshelf);

        //Vbox for left elements
        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.getChildren().addAll(displayTitle, displayAuthor);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(comboBookshelf);
        hBox.setSpacing(30);



        hbox.getChildren().addAll(vBox, hBox);

        return hbox;
    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);


        //description

        TextArea description = new TextArea("Short Description");
        description.setText(book.getDescription());
        description.setWrapText(true);
        //description.setPrefColumnCount(15);
        description.setFont(Font.font(null, FontWeight.NORMAL, 12));
        description.setEditable(false);


        //scroll for description textarea

        ScrollPane scrollPane = new ScrollPane(description);
        scrollPane.getStyleClass().add("scrollDescription");
        //scrollPane.setPrefSize(200,200);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        //Review Area
        Label reviewLabel = new Label();
        reviewLabel.setText("{star_widget}");
        TextArea review = new TextArea("review");
        review.setText("review");
        review.setFont(Font.font(null, FontWeight.NORMAL, 12));
        review.setWrapText(true);
        review.setPrefColumnCount(15);
        review.setEditable(true);


        //scroll for review textarea
        ScrollPane scrollPane2 = new ScrollPane(review);
        scrollPane2.getStyleClass().add("scrollReview");
        scrollPane2.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);

        //Vbox for review
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(10);
        reviewBox.getChildren().addAll(reviewLabel, scrollPane2);

        //left side
        VBox left = new VBox();
        left.setSpacing(20);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane2, Priority.ALWAYS);
        left.getChildren().addAll(scrollPane);

        //image
        imageView.setFitWidth(150);
        imageView.setFitHeight(220);
        imageView.setPreserveRatio(false);
        HBox.setHgrow(imageView, Priority.ALWAYS);
        VBox.setVgrow(imageView, Priority.ALWAYS);

        //overall view
        HBox overall = new HBox();

        overall.setSpacing(50);
        overall.getChildren().addAll(imageView, left, reviewBox);


        //tags
        HBox tagBox = new HBox();
        Button tagButton = new Button();
        tagButton.setText("Tag");

        tagBox.setSpacing(5);
        tagBox.getChildren().addAll(tagButton);

        vbox.getChildren().addAll(overall, tagBox);


        return vbox;
    }


}





