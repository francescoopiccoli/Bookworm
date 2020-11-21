package com.Bookworm.ui.views;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.ui.widgets.BookListWidget;
import com.Bookworm.ui.widgets.BookWidget;

import com.Bookworm.ui.widgets.BookshelfWidget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
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
            ArrayList<Book> allbooks = new ArrayList<>();
            for(Book b : db.getBooks()){
                allbooks.add(b);
            }

            Bookshelf def = new Bookshelf("Default","Default Bookshelf", allbooks);
            bookshelveswithdefault.add(0,def);
            setCenter(new BookshelfWidget(bookshelveswithdefault,this));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            root.setSpacing(5);
            Label name = new Label("Insert the name of your Bookshelf");
            TextField nameField = new TextField();

            root.getChildren().addAll(name, nameField);
            Label Description = new Label("Insert a description for your bookshelf");
            TextField descfield = new TextField();

            root.getChildren().addAll(Description, descfield);
            BorderPane pane = new BorderPane();
            Button commit = new Button("Create the new Bookshelf");
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
                    if(flagExist==false){
                        System.out.println("Bookshelf inserted!");
                        db.insertBookshelf(list);
                    }
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                //ADD code that creates new List
                test.hide();

                //Reload central part with lists
                try {
                    List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                    ArrayList<Book> allbooks = new ArrayList<>();
                    for(Book b : db.getBooks()){
                        allbooks.add(b);
                    }

                    Bookshelf def = new Bookshelf("Default","Default Bookshelf", allbooks);
                    bookshelveswithdefault.add(0,def);
                    setCenter(new BookshelfWidget(bookshelveswithdefault,this));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            });
            pane.setCenter(commit);
            root.getChildren().add(pane);
            test.setScene(new Scene(root, 450, 250));
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

        return sc;
    }

    public Node createTop2(String description) {
        Button back = new Button("Go Back");
        back.setOnMouseClicked(event -> {
            setTop(createTop());
            //setCenter(createCenter());
            try {
                List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                ArrayList<Book> allbooks = new ArrayList<>();
                for(Book b : db.getBooks()){
                    allbooks.add(b);
                }

                Bookshelf def = new Bookshelf("Default","Default Bookshelf", allbooks);
                bookshelveswithdefault.add(0,def);
                setCenter(new BookshelfWidget(bookshelveswithdefault,this));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        Text t = new Text(description);

        BorderPane borderPane = new BorderPane();
        borderPane.setRight(back);
        borderPane.setLeft(t);
        HBox.setHgrow(borderPane,Priority.ALWAYS);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(borderPane);
        hBox.setPadding(new Insets(15, 12, 15, 12));

        return hBox;
    }

    //Not used anymore
    public Node createTop2() {


        Button back = new Button("Go Back");
        back.setOnMouseClicked(event -> {
            setTop(createTop());
            //setCenter(createCenter());
            try {
                List<Bookshelf> bookshelveswithdefault = db.getBookShelves();
                ArrayList<Book> allbooks = new ArrayList<>();
                for(Book b : db.getBooks()){
                    allbooks.add(b);
                }

                Bookshelf def = new Bookshelf("Default","Default Bookshelf", allbooks);
                bookshelveswithdefault.add(0,def);
                setCenter(new BookshelfWidget(bookshelveswithdefault,this));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BorderPane pane = new BorderPane();
        BookListView blv = new BookListView("Default Bookshelf",books);
        pane.setCenter(blv);
        return pane;

    }
    public Node createCenter3(String bookshelf) {
        List<Book> books = null;
        books = (queryBooksOfBookshelf(bookshelf));
        BorderPane pane = new BorderPane();
        BookListView blv = new BookListView(bookshelf,books);
        pane.setCenter(blv);
        return pane;

    }
    private List<Book> queryBooksOfBookshelf(String bookshelf){
        List<Book> books = null;
        try {
            books = db.getBookShelfBooks(db.getBookshelfID(bookshelf));
            //String.valueOf((db.getBookshelfID(bookshelf)))
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Book b: books
             ) {
            System.out.println("BOOK : ");

        }
        return books;
    }

}
