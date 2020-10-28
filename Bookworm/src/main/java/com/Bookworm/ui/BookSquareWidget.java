package com.Bookworm.ui;

import com.Bookworm.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BookSquareWidget extends BorderPane {
    Book book;
    private Image image;

    public BookSquareWidget(Book book) {
        this.book = book;
        image = new Image(book.getImageURL());
        ImageView imageView = new ImageView(this.image);
        setCenter(imageView);

        Text t = new Text(book.getName());
        t.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        setMargin(t, new Insets(20));
        setAlignment(t, Pos.BOTTOM_CENTER);
        setBottom(t);

        //setPadding(new Insets(10));
        setStyle("-fx-background-color: linear-gradient(to top, #ccc, #fff); -fx-border-color: #999;");
    }

    public Book getBook() {
        return book;
    }

    public Image getImage() {
        return image;
    }
}
