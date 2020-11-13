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
    private static BookInfoView biv;

    private static HBox generateWidget() throws SQLException, ClassNotFoundException {

        if(db.getBook(book.getName(), book.getAuthor()) != null){
            rating = db.getBook(book.getName(), book.getAuthor()).getRating();
        }
        else{
            rating = book.getRating();
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
                    updateWidget(newRating);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });
        }

        return starWidgetBox;
    }

    private static void updateWidget(int newRating) throws SQLException, ClassNotFoundException {
        db.insertRating(book, newRating);
        rating = newRating;
        biv.refresh();
    }

    public static HBox getStarWidget(BookInfoView parent, Book b) throws SQLException, ClassNotFoundException {
        book = b;
        biv = parent;
        starWidgetBox = new HBox();
        starWidgetBox.getStylesheets().add(StarWidget.class.getResource("/Stylesheets/style.css").toExternalForm());
        return generateWidget();
    }
}
