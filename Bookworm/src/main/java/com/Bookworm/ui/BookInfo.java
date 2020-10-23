package com.Bookworm.ui;


import com.Bookworm.model.Book;
import com.Bookworm.model.Tag;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.*;

public class BookInfo extends BorderPane {


    String title;
    String bookDescription;
    String author,url;
    public static LinkedList<String> authors = new LinkedList<String>();




    public BookInfo(String title, String author, String bookDescription, String url) {

        this.title=title;
        this.author=author;
        this.bookDescription=bookDescription;
        this.url = url;

        setTop(addHBoxTop());
        setCenter(addVBox());

    }

    public HBox addHBoxTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(40);
        VBox vb = new VBox();

        //book title

        TextArea displayTitle = new TextArea(title);
        displayTitle.setPrefSize(200, 30);
        displayTitle.setEditable(false);
        displayTitle.setMouseTransparent(true);
        displayTitle.setFocusTraversable(false);

        //tags
        Button tag1 = new Button("tag1");
        Button tag2 = new Button("tag2");
        Button add = new Button("+");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(tag1, tag2,add);
        buttons.setSpacing(10);
        vb.setSpacing(10);
        vb.getChildren().addAll(displayTitle,buttons);

        //author name

        ComboBox combo_box = new ComboBox();


        /*to be checked
        for (String author : authors){
            authors.addAll(Collections.singleton(author));
        }
        combo_box.getItems().addAll(authors);
        */


        combo_box.getItems().addAll("");

        combo_box.setOnAction(e -> combo_box.getValue());
        combo_box.setEditable(false);

        ImageView imageView = new ImageView(url);
        imageView.setFitWidth(150);

        imageView.setFitHeight(200);

        hbox.getChildren().addAll(vb,combo_box, imageView);

        return hbox;
    }
    public VBox addVBox()
    {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);


        //description

        TextArea description = new TextArea("Short Description");
        description.setText(bookDescription);
        description.setPrefColumnCount(15);
        description.setPrefSize(100,100);
        description.setEditable(false);
        description.setMouseTransparent(true);
        description.setFocusTraversable(false);

        //bookshelf
        Button button1 = new Button("add to #1");
        Button button2 = new Button("add to #2");
        HBox bookshelf = new HBox();
        bookshelf.getChildren().addAll(button1,button2);
        bookshelf.setSpacing(10);


        //Review Area
        TextArea review = new TextArea("review");
        review.setText("review");
        review.setPrefColumnCount(15);
        review.setPrefSize(100,100);
        review.setEditable(true);
        Button bReview = new Button("Review");

        //right side
        VBox reviewVB = new VBox();
        reviewVB.setSpacing(10);
        reviewVB.getChildren().addAll(review,bReview);

        //left side
        VBox left = new VBox();
        left.setSpacing(20);
        left.getChildren().addAll(description,bookshelf);

        //overall view
        HBox overall = new HBox();
        overall.setSpacing(80);
        overall.getChildren().addAll(left,reviewVB);

        vbox.getChildren().addAll(overall);




        return vbox;
    }


}







/*

          //book cover
        Image image = new Image("http://placekitten.com/200/300");
        ImageView iv = new ImageView(image);
        iv.setFitHeight(80);
        iv.setFitWidth(80);
 */