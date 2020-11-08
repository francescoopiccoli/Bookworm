package com.Bookworm.ui;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Bookshelf;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Booklist extends BorderPane {

    public Booklist() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTop());
        setCenter(createCenter());

    }

    private Node createTop() {

        Label title = new Label("Your Bookshelves");
        title.getStyleClass().add("titleBookshelf");
        Button addbutton = new Button("Create new Bookshelf");
        addbutton.setOnMouseClicked(event -> {
            Stage test = new Stage();
            test.setTitle("My New Stage Title");
            VBox root = new VBox();
            root.getStylesheets().add(getClass().getResource("/Stylesheets/style.css").toExternalForm());
            root.setSpacing(5);
            Label name = new Label("Enter the name of the new Bookshelf");
            TextField nameField = new TextField();

            root.getChildren().addAll(name, nameField);
            Label Description = new Label("Description for new Bookshelf");
            TextArea descfield = new TextArea();

            root.getChildren().addAll(Description, descfield);
            BorderPane pane = new BorderPane();
            Button commit = new Button("Create the new Bookshelf");
            commit.getStyleClass().add("commitBookshelfButton");
            commit.setOnMouseClicked(e -> {


                //ADD code that creates new List
                test.hide();

                //Reload central part with lists
                setCenter(createCenter());

            });
            pane.setCenter(commit);
            root.getChildren().add(pane);
            test.setScene(new Scene(root, 450, 250));
            test.show();
        });

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        HBox hBox = new HBox(region1, title, region2, addbutton);
        hBox.getStyleClass().add("hbTopBookshelf");


        return hBox;
    }

    private Node createCenter() {
        ScrollPane sc = new ScrollPane();
        sc.setFitToHeight(true);
        sc.getStyleClass().add("scrollCenterLists");
        VBox vb = new VBox();
        vb.setSpacing(25);
        vb.getStyleClass().add("vbLists");
        //Add default bookshelf
        Button defaultshelf = new Button("Default - Bookshelf");
        defaultshelf.setOnMouseClicked(event -> {
            setTop(createTop2("Defaulf - Bookshelf"));
            setCenter(createCenter2());
        });
        defaultshelf.getStyleClass().add("ListsButton");
        vb.getChildren().add(defaultshelf);
        //Placeholder for method getting all existing bookshelves
            //Placeholder for will have to iterate over list of all shelves
            try {
                LinkedList<Bookshelf> b = (LinkedList<Bookshelf>) DatabaseManager.getInstance().getBookShelves();
                System.out.println(b.isEmpty());
                for(Bookshelf bookshelf : b) {
                    System.out.println(bookshelf.getName() + "ciao");
                    Button list = new Button(bookshelf.getName());
                    list.getStyleClass().add("ListsButton");
                    vb.getChildren().add(list);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        sc.setContent(vb);

        return sc;
    }


    public Node createTop2(String title) {

        Label t = new Label(title);
        t.getStyleClass().add("titleBookshelf2");
        Button back = new Button("Return - Go back");
        back.setOnMouseClicked(event -> {
            setTop(createTop());
            setCenter(createCenter());
        });;


        HBox hBox = new HBox(back,t);
        hBox.getStyleClass().add("hbTopBookshelf");
        return hBox;
    }

    public Node createCenter2() {
        ScrollPane sc = new ScrollPane();
        sc.getStyleClass().add("scrollCenterLists");
        return sc;
    }

}
