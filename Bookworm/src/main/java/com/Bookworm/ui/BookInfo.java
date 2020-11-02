package com.Bookworm.ui;


import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.model.Tag;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.*;

public class BookInfo extends BorderPane {


    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 350;


    String title;
    String bookDescription;
    String author;
    ImageView url;
    String category;
    int rating;
    ArrayList<Tag> tags;
    public static ArrayList<Bookshelf> bookShelf = new ArrayList<>();
    public static List<Book> bookList = new LinkedList<>();
    private  List<BookSquareWidget> books;

    public static List<Book> getBookList() {
        return bookList;
    }

    public static void setBookList(List<Book> bookList) {
        BookInfo.bookList = bookList;
    }

    public List<BookSquareWidget> getBooks() {
        return books;
    }

    public void setBooks(List<BookSquareWidget> books) {
        this.books = books;
    }

    public static ArrayList<Bookshelf> getBookShelf() {
        return bookShelf;
    }

    public static void setBookShelf(ArrayList<Bookshelf> bookShelf) {
        BookInfo.bookShelf = bookShelf;
    }


    public BookInfo(String title, String author, String bookDescription, String category, ArrayList<Tag> tags, int rating, ImageView url) {
        this.title = title;
        this.author = author;
        this.bookDescription = bookDescription;
        this.category = category;
        this.tags = tags;
        this.rating = rating;
        this.url = url;

        setTop(addHBoxTop());
        setCenter(addVBox());

    }

    public static void spawnWindow(Book book) {
        spawnWindow(book, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static void spawnWindow(Book book, Image image) {
        spawnWindow(book, DEFAULT_WIDTH, DEFAULT_HEIGHT, image);
    }

    public static void spawnWindow(Book book, int w, int h) {
        Image image = new Image(book.getImageURL());
        spawnWindow(book, w, h, image);
    }

    public static void spawnWindow(Book book, int w, int h, Image image) {
        ImageView imageView = new ImageView(image);
        BookInfo bookInfo = new BookInfo(book.getName(), book.getAuthor(), book.getDescription(), book.getCategory(), book.getTags(), book.getRating(), imageView);

        Stage stage = new Stage();
        stage.setTitle(book.getName());
        Scene scene = new Scene(bookInfo, w, h);
        stage.setScene(scene);
        stage.show();
    }

    public HBox addHBoxTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(40);

        //book title

        Label displayTitle = new Label();
        displayTitle.setText("Title: " + title);
        displayTitle.getStyleClass().add("display");
        displayTitle.setFont(Font.font(null, FontWeight.BOLD, 10));


        //book author

        Label displayAuthor = new Label();
        displayAuthor.setText("Author: " + author);
        displayAuthor.getStyleClass().add("display");
        displayAuthor.setFont(Font.font(null, FontWeight.BOLD, 10));


        //tags
        HBox tagBox = new HBox();
        Button tagButton = new Button();
        tagButton.setText("Tag");

        tagBox.setSpacing(5);
        tagBox.getChildren().addAll(tagButton);


        //display category
        Label displayCategory = new Label();
        displayCategory.setText("Category: " + category);
        displayCategory.getStyleClass().add("display");
        displayCategory.setFont(Font.font(null, FontWeight.BOLD, 10));

        //bookshelf
        HBox bookshelfBox = new HBox();
        Label bookshelfLabel = new Label();
        bookshelfLabel.setText("Add to");
        ComboBox bookshelfCombo = new ComboBox();
        ComboBox<Bookshelf> comboBookshelf = new ComboBox<Bookshelf>();

        //list stays empty need to check later on
        if (!bookShelf.isEmpty()) {
            comboBookshelf.setItems(FXCollections.observableArrayList(bookShelf));
            comboBookshelf.setConverter(new StringConverter<Bookshelf>() {

                @Override
                public String toString(Bookshelf bookshelf) {
                    return bookshelf.getName();
                }

                @Override
                public Bookshelf fromString(String s) {
                    return null;
                }
            });
        }
        comboBookshelf.valueProperty().addListener((obs, oldVal, newVal) -> {
        String txt = newVal.getName();
        System.out.println(txt);
                });
        //button to add in a bookshelf
        Button add = new Button();
        add.setText("Add");
        add.setOnMouseClicked(e->{
        //implement to add to a bookshelf

        });

        //add to reading list
        HBox readingList=new HBox();
        readingList.setSpacing(5);
        Label reading = new Label();
        reading.setText("Add to reading list");
        Button addBook = new Button();
        addBook.setText("+");
        addBook.setOnMouseClicked(e->{

        });
        readingList.getChildren().addAll(reading,addBook);


        //vbox for bookshelf
        VBox vboxBookshelf = new VBox();
        vboxBookshelf.setSpacing(8);
        vboxBookshelf.getChildren().addAll(comboBookshelf,add);


        bookshelfBox.setSpacing(20);
        bookshelfBox.getChildren().addAll(bookshelfLabel, vboxBookshelf,readingList);

        //Vbox for left elements
        VBox vBox = new VBox();
        vBox.setSpacing(8);
        vBox.getChildren().addAll(displayTitle, displayAuthor, displayCategory);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(vboxBookshelf,readingList);
        hBox.setSpacing(30);

        hbox.getChildren().addAll(vBox,tagBox, hBox);

        return hbox;
    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);


        //description

        TextArea description = new TextArea("Short Description");
        description.setText(bookDescription);
        description.setWrapText(true);
        description.setPrefColumnCount(15);
        description.setPrefWidth(150);
        description.setPrefHeight(200);
        description.setFont(Font.font(null, FontWeight.NORMAL, 12));
        description.setEditable(false);


        //scroll for description textarea

        ScrollPane scrollPane = new ScrollPane(description);
        scrollPane.getStyleClass().add("scrollDescription");
        //scrollPane.setPrefSize(200,200);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        //Vbox for description
        VBox vBoxD = new VBox();
        vBoxD.setSpacing(10);
        vBoxD.getChildren().addAll(scrollPane);


        //Review Area
        Label reviewLabel = new Label();
        reviewLabel.setText("Add review");
        TextArea review = new TextArea("review");
        review.setText("review");
        review.setFont(Font.font(null, FontWeight.NORMAL, 12));
        review.setWrapText(true);
        review.setPrefWidth(150);
        review.setPrefHeight(200);
        review.setPrefColumnCount(15);
        review.setEditable(true);
        Button bReview = new Button("Review");

        //scroll for review textarea
        ScrollPane scrollPane2 = new ScrollPane(review);
        scrollPane2.getStyleClass().add("scrollReview");
        scrollPane2.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);

        //Vbox for review
        VBox reviewBox = new VBox();
        reviewBox.setSpacing(10);
        reviewBox.getChildren().addAll(reviewLabel, scrollPane2);

        //right side
        VBox reviewVB = new VBox();
        reviewVB.setSpacing(10);
        reviewVB.getChildren().addAll(reviewBox, bReview);

        //left side
        VBox left = new VBox();
        left.setSpacing(20);
        left.getChildren().addAll(vBoxD);

        //image
        url.setFitWidth(150);
        url.setFitHeight(200);

        //overall view
        HBox overall = new HBox();

        overall.setSpacing(50);
        overall.getChildren().addAll(url, left, reviewVB);

        vbox.getChildren().addAll(overall);


        return vbox;
    }


}





