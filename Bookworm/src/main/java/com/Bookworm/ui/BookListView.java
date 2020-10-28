package com.Bookworm.ui;

import com.Bookworm.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BookListView extends BorderPane {
    private final String title;
    private final List<BookSquareWidget> books;
    private final ScrollPane scrollPane;
    public BookListView(String title, List<Book> books) {
        super();
        this.title = title;
        this.books = new ArrayList<>();
        for(Book book : books) {
            this.books.add(new BookSquareWidget(book));
        }

        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(20));
        setTop(headerPane);

        Text t = new Text(title);
        t.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, 18));
        headerPane.setLeft(t);


        TextField textField = new TextField();
        textField.setPromptText("dragons, cats, tolkien...");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = newValue;
            _updateList(filter);
        });

        headerPane.setRight(textField);
        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        _updateList();

    }

    public String getTitle() {
        return title;
    }

    private void _updateList() {
        _updateList("");
    }
    private void _updateList(String filter) {
        if (books == null || books.size() == 0) {
            setCenter(new Label("This list feels so empty :("));
        } else {
            VBox vb = new VBox();
            vb.setAlignment(Pos.CENTER);
            int i = 0;
            HBox hb = new HBox();
            for (BookSquareWidget b : books) {
                if (i > 2) {
                    i = 0;
                }
                if (i == 0) {
                    hb = new HBox();
                    hb.setAlignment(Pos.CENTER);
                    vb.getChildren().add(hb);
                }

                // filtering
                if (!b.getBook().getName().toLowerCase().contains(filter.toLowerCase())
                        && !b.getBook().getDescription().toLowerCase().contains(filter.toLowerCase()))
                    continue;

                b.setOnMouseClicked((event -> {
                    Book book = b.getBook();
                    // bookInfo should accept Book object, not just its parameters!

                    BookInfo.spawnWindow(book, 600, 300, b.getImage());
                }));

                HBox.setMargin(b, new Insets(10));
                hb.getChildren().add(b);

                i++;
            }
            scrollPane.setContent(vb);
            setCenter(scrollPane);
        }
    }
}
