package com.Bookworm.ui.widgets;

import com.Bookworm.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BookWidget extends BorderPane {
    public static final String PLACEHOLDER_IMAGE_URI = "/Images/placeholder.png";

    Book book;
    private Image image;
    private ImageView imageView;

    public BookWidget(Book book) {
        this.book = book;

        image = new Image(getClass().getResourceAsStream(BookWidget.PLACEHOLDER_IMAGE_URI));
        imageView = new ImageView(this.image);
        imageView.setFitHeight(250);
        imageView.setFitWidth(180);
        setCenter(imageView);

        // is this responsible for the weird NullPointerException stuff?

        try {
            if(book.getImageURL() == ""){
                image = new Image(getClass().getResourceAsStream(BookWidget.PLACEHOLDER_IMAGE_URI));
            }
            else{
                image = new Image(book.getImageURL(), true);
            }
            imageView.setImage(image);
        } catch (IllegalArgumentException | NullPointerException e) {
            image = new Image(getClass().getResourceAsStream(BookWidget.PLACEHOLDER_IMAGE_URI));
        }

        if(book != null) {
            String title = book.getName();
            if (title.length() > 20) {
                title = title.substring(0, 19) + "\u2026";
            }
            Text t = new Text(title);

            t.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            setMargin(t, new Insets(20));
            setAlignment(t, Pos.BOTTOM_CENTER);
            t.getStyleClass().add("BookTitle");
            setBottom(t);
        }
        //setPadding(new Insets(10));
        setStyle("-fx-background-color: #722620; -fx-border-color: #30110D;");
    }

    public Book getBook() {
        return book;
    }

    public Image getImage() {
        return image;
    }

}
