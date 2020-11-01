package com.Bookworm.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

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
        });;
        defaultshelf.getStyleClass().add("ListsButton");
        vb.getChildren().add(defaultshelf);
        //Placeholder for method getting all existing bookshelves
        if(true){
            //Placeholder for will have to iterate over list of all shelves
            for(int i = 0; i<8; i++) {
                Button list = new Button("Placeholder");
                list.getStyleClass().add("ListsButton");
                vb.getChildren().add(list);
            }
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
        return sc;
    }

    public void refresh() {
        setCenter(createCenter());
    }
}
