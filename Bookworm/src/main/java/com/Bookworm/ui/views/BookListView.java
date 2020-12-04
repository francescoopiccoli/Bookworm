package com.Bookworm.ui.views;

import com.Bookworm.model.Book;
import com.Bookworm.ui.widgets.BookListWidget;
import com.Bookworm.ui.widgets.BookWidget;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class BookListView extends BorderPane {
    private final String title;
    private List<BookWidget> books;

    private final BookListWidget listWidget;
    private String currentFilter = "";

    public BookListView(String title, String desc, List<Book> books) {
        super();
        this.title = title;
        this.books = new ArrayList<>();
        for(Book book : books) {
            // we could even maybe async-ize this thing as well?
            this.books.add(new BookWidget(book));
        }

        BorderPane headerPane = new BorderPane();
        headerPane.setPadding(new Insets(20));
        setTop(headerPane);

        Label t = new Label(title);
        t.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, 18));
        headerPane.setLeft(t);

        Label t2 = new Label(desc);
        t2.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, 13));
        headerPane.setBottom(t2);

        listWidget = new BookListWidget(books);
        setCenter(listWidget);
        listWidget.updateList();

        TextField textField = new TextField();
        textField.setPromptText("Filter");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = newValue;
            listWidget.updateList(filter);
        });

        headerPane.setRight(textField);

    }

    public BookListWidget getListWidget() {
        return listWidget;
    }

    public String getTitle() {
        return title;
    }

    public List<BookWidget> getBooks() {
        return books;
    }
    public void setBooks(List<BookWidget> books) {
        this.books = books;
    }

}
