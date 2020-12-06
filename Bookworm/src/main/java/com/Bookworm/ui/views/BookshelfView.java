package com.Bookworm.ui.views;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.ui.widgets.BookshelfWidget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookshelfView extends BorderPane {
    private DatabaseManager db = BookInfoView.dbManager;
    boolean flagExist = false;
    private List<Bookshelf> bookshelves;
    private List<ImageView> covers = new ArrayList<>();
    private Image image;
    public BookshelfView() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTop());
        try {
            List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
            setCenter(new BookshelfWidget(bookshelveswithdefault,this));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        //setCenter(createCenter());

    }


    private Node createTop() {

        //principal view
        Label title = new Label();
        title.setText("Your Bookshelves");
        title.setStyle("-fx-font-size: 16;-fx-font-weight:bold");
        Label descriptionText = new Label();
        descriptionText.setText("Check out your customized bookshelves");
        Button addbutton = new Button("+");
        addbutton.setStyle("-fx-font-size: 16;-fx-font-weight:bold");

        //event on add button
        addbutton.setOnMouseClicked(event -> {

           // pop up view
            Stage test = new Stage();
            test.setTitle("Add Bookshelf");
            VBox root = new VBox();
            root.getStylesheets().add(getClass().getResource("/Stylesheets/style.css").toExternalForm());
            //root.getStylesheets().add(getClass().getResource("/Stylesheets/style2.css").toExternalForm());
            root.setSpacing(5);
            Label name = new Label("Name");
            TextField nameField = new TextField();
            nameField.setPrefWidth(10);

            root.getChildren().addAll(name, nameField);
            Label Description = new Label("Description");
            TextField descfield = new TextField();

            root.getChildren().addAll(Description, descfield);
            BorderPane pane = new BorderPane();
            Button commit = new Button("Create");
            commit.getStyleClass().add("commitBookshelfButton");
            flagExist = false;
            commit.setOnMouseClicked(e -> {
                Bookshelf list = new Bookshelf(nameField.getText(),descfield.getText());
                try {
                    //int bookshelfID = db.getBookshelfID(nameField.getText());
                    List<Bookshelf> allBookshelves = db.getBookShelves();

                    for (Bookshelf b : allBookshelves) {
                        if(b.getName().contentEquals(list.getName())){
                            flagExist=true;
                            System.out.println("Bookshelf already exists");
                        }
                    }
                    if(!flagExist){
                        System.out.println("Bookshelf inserted!");
                        db.insertBookshelf(list);
                    }
                } catch (ClassNotFoundException | SQLException e1) {
                    e1.printStackTrace();
                }
                //ADD code that creates new List
                test.hide();

                //Reload central part with lists
                try {
                    List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                    setCenter(new BookshelfWidget(bookshelveswithdefault,this));
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }

            });
            pane.setCenter(commit);
            root.getChildren().add(pane);
            test.setScene(new Scene(root, 450, 175));
            test.show();
        });

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);



        VBox vb = new VBox();
        vb.getChildren().addAll(title,descriptionText);
        vb.setSpacing(1);
        vb.setAlignment(Pos.TOP_LEFT);
        HBox hBox = new HBox(vb, region1, addbutton);
        hBox.setPadding(new Insets(15, 12, 15, 12));

        return hBox;
    }

    private Node createCenter(Bookshelf bookshelf) {
        ScrollPane sc = new ScrollPane();
        sc.setFitToHeight(true);
        sc.getStyleClass().add("inner");
        return sc;
    }


    public Node createTop2(String description, Bookshelf bookshelf) {
        Button deleteButton = new Button("Delete Bookshelf");
        deleteButton.setOnMouseClicked(event -> {
            try {
                ArrayList<Book> books = bookshelf.getBooks();
                if (books != null) {
                    for(Book b: books) {
                        db.deleteBook(b);
                        //start
                       // db.insertBook(b, "Default");
                    }
                }
                db.deleteBookshelf(bookshelf);
                deleteButton.setText("Click on Back");
                //BookListView.getListWidget().updateList();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

        Button back = new Button("🡰 Back");
        back.setOnMouseClicked(event -> {
            setTop(createTop());
            //setCenter(createCenter());
            try {
                List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                setCenter(new BookshelfWidget(bookshelveswithdefault,this));
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
        Text t = new Text(description);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(back);
        System.out.println(bookshelf.getId());
        if (bookshelf.getId()!=1)borderPane.setRight(deleteButton);
        //borderPane.setRight(back);
        //borderPane.setLeft(t);
        HBox.setHgrow(borderPane,Priority.ALWAYS);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(borderPane);
        hBox.setPadding(new Insets(15, 12, 15, 12));

        return hBox;
    }

    //Not used anymore
    public Node createTop2() {


        Button back = new Button("🡰 Back");
        back.setOnMouseClicked(event -> {
            setTop(createTop());
            //setCenter(createCenter());
            try {
                List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                setCenter(new BookshelfWidget(bookshelveswithdefault,this));
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setRight(back);
        HBox.setHgrow(borderPane,Priority.ALWAYS);
        HBox hBox = new HBox(borderPane);
        hBox.setPadding(new Insets(15, 12, 15, 12));

        return hBox;
    }
    public Node createCenter2() {
        List<Book> books = null;
        try {
            books = db.getBooks();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        BorderPane pane = new BorderPane();
        BookListView blv = new BookListView("Default Bookshelf","", books);
        pane.setCenter(blv);
        return pane;
    }

    public Node createCenter3(String bookshelf, String desc) {
        List<Book> books;
        books = (queryBooksOfBookshelf(bookshelf));
        BorderPane pane = new BorderPane();
        BookListView blv = new BookListView(bookshelf,desc,books);
        pane.setCenter(blv);
        return pane;
    }

    private List<Book> queryBooksOfBookshelf(String bookshelf){
        List<Book> books = null;
        try {
            books = db.getBookShelfBooks(db.getBookshelfID(bookshelf));
            //String.valueOf((db.getBookshelfID(bookshelf)))
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        for (Book b: books) {
            System.out.println("BOOK : ");

        }
        return books;
    }

}
