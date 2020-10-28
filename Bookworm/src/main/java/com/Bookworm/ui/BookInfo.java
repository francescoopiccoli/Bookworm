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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.*;

public class BookInfo extends BorderPane {


    String title;
    String bookDescription;
    String author;
    ImageView url;





    public BookInfo(String title, String author, String bookDescription, ImageView url) {

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

        //book title

        Label displayTitle = new Label();
        displayTitle.setText("Title: "+ title);
        displayTitle.getStyleClass().add("display");
        displayTitle.setFont(Font.font(null, FontWeight.BOLD, 15));


        //book author

        Label displayAuthor = new Label();
        displayAuthor.setText("Author: "+ author);
        displayAuthor.getStyleClass().add("display");
        displayAuthor.setFont(Font.font(null, FontWeight.BOLD, 15));

        //tags
        Button tag1 = new Button("tag1");
        Button tag2 = new Button("tag2");
        Button add = new Button("+");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(tag1, tag2,add);
        buttons.setSpacing(10);


        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(displayTitle,displayAuthor);



        hbox.getChildren().addAll(vBox, buttons);

        return hbox;
    }
    public VBox addVBox()
    {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);


        //description
        Label descLabel = new Label();
        descLabel.setText("Description");
        TextArea description = new TextArea("Short Description");
        description.setText(bookDescription);
        description.setWrapText(true);
        description.setPrefColumnCount(15);

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
        vBoxD.getChildren().addAll(descLabel,scrollPane);

        //bookshelf
        Button button1 = new Button("add to #1");
        Button button2 = new Button("add to #2");
        HBox bookshelf = new HBox();
        bookshelf.getChildren().addAll(button1,button2);
        bookshelf.setSpacing(10);


        //Review Area
        Label reviewLabel = new Label();
        reviewLabel.setText("Add review");
        TextArea review = new TextArea("review");
        review.setText("review");
        review.setWrapText(true);
        review.setPrefColumnCount(15);
        review.setPrefSize(200,200);
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
        reviewBox.getChildren().addAll(reviewLabel,scrollPane2);

        //right side
        VBox reviewVB = new VBox();
        reviewVB.setSpacing(10);
        reviewVB.getChildren().addAll(reviewBox,bReview);

        //left side
        VBox left = new VBox();
        left.setSpacing(20);
        left.getChildren().addAll(vBoxD,bookshelf);

        //image
        url.setFitWidth(150);
        url.setFitHeight(200);

        //overall view
        HBox overall = new HBox();

        overall.setSpacing(80);
        overall.getChildren().addAll(url,left,reviewVB);

        vbox.getChildren().addAll(overall);




        return vbox;
    }


}





