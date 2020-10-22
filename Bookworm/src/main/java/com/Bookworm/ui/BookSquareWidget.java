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
import javafx.scene.text.TextAlignment;

public class BookSquareWidget extends BorderPane {
    Book book;

    public BookSquareWidget(Book book) {
        this.book = book;
        double cacheBuster = Math.random();
        System.out.println("Loading kitten... "+cacheBuster);
        ImageView image = new ImageView(new Image("https://cataas.com/cat?type=sq&"+cacheBuster));
        setCenter(image);

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
}
