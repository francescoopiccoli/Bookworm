package com.Bookworm.ui.widgets;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.ui.views.BookInfoView;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StarWidget {

    public static final String STAR_EMPTY = "/Images/star-empty.png";
    public static final String STAR_FILLED = "/Images/star-filled.png";
    private static HBox starWidgetBox;
    private static Image emptyStar = new Image(StarWidget.class.getResourceAsStream(STAR_EMPTY));
    private static Image filledStar = new Image(StarWidget.class.getResourceAsStream(STAR_FILLED));
    private static Book book;
    private static int rating = 0;
    private static ImageView imageview;
    private static Button b;
    private static DatabaseManager db = DatabaseManager.getInstance();
    private static BookInfoView parent;
    private static final Logger LOGGER = Logger.getLogger(StarWidget.class.getName());

    //generate the correct widget: according to the rating, draw the correct number of filled / empty stars
    private static HBox generateWidget(){
        try {
            if(db.getBook(book.getName(), book.getAuthor()) != null){
                rating = db.getBook(book.getName(), book.getAuthor()).getRating();
            }
            else{
                rating = book.getRating();
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            LOGGER.log( Level.SEVERE, throwables.toString(), throwables);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            LOGGER.log( Level.SEVERE, e.toString(), e);
        }

        for(int i = 0; i < 5; i++){
            b = new Button();
            b.getStyleClass().add("starButton");

            if(i < rating){
                imageview = new ImageView(filledStar);
                b.setGraphic(imageview);
            }

            else{
                imageview = new ImageView(emptyStar);
                b.setGraphic(imageview);
            }

            imageview.setFitWidth(18);
            imageview.setFitHeight(18);
            starWidgetBox.getChildren().add(b);
            starWidgetBox.setHgrow(b, Priority.ALWAYS);

            int newRating = i+1;
            b.setOnAction(e -> {
                try {
                    if(db.getBook(book.getName(), book.getAuthor()) != null){
                        updateWidget(newRating);
                    }
                } catch (SQLException throwables) {
                    //throwables.printStackTrace();
                    LOGGER.log( Level.SEVERE, throwables.toString(), throwables);

                } catch (ClassNotFoundException classNotFoundException) {
                    //classNotFoundException.printStackTrace();
                    LOGGER.log( Level.SEVERE, classNotFoundException.toString(), classNotFoundException);
                }
            });
        }

        return starWidgetBox;
    }



    //updates bookinfo by inserting new rating value in the database and refreshing the bookinfo window
    private static void updateWidget(int newRating){
        try {
            db.insertRating(book, newRating);
        } catch (SQLException throwables) {
            LOGGER.log( Level.SEVERE, throwables.toString(), throwables);
            //throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            LOGGER.log( Level.SEVERE, e.toString(), e);
            //e.printStackTrace();
        }
        rating = newRating;
        parent.refresh();
    }


    //method called in bookinfo to create the initial starwidget
    public static HBox getStarWidget(BookInfoView parentView, Book b){
        book = b;
        parent = parentView;
        starWidgetBox = new HBox();
        starWidgetBox.getStylesheets().add(StarWidget.class.getResource("/Stylesheets/style.css").toExternalForm());
        return generateWidget();
    }
}
