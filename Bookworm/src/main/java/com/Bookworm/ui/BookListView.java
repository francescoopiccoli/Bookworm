package com.Bookworm.ui;

import com.Bookworm.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BookListView extends BorderPane {
    private final String title;
    private List<BookSquareWidget> books;
    private final ScrollPane scrollPane;
    private String currentFilter = "";

    public BookListView(String title, List<Book> books) {
        super();
        this.title = title;
        this.books = new ArrayList<>();
        for(Book book : books) {
            // we could even maybe async-ize this thing as well?
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
            updateList(filter);
        });

        headerPane.setRight(textField);
        scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        scrollPane.widthProperty().addListener((e) ->  updateList(null) );
        updateList();

    }

    public String getTitle() {
        return title;
    }

    public void updateList() {
        updateList("");
    }
    public void updateList(String filter) {
        if(filter != null)
            currentFilter = filter;

        if (books == null || books.size() == 0) {
            setCenter(new Label("This list feels so empty :("));
        } else {
            VBox vb = new VBox();
            scrollPane.setContent(vb);
            vb.setAlignment(Pos.CENTER);

            int width = (int) scrollPane.getWidth();
            int numColumns = getMaxColumns(width);

            int i = 0;
            HBox hb = new HBox();
            for (BookSquareWidget b : books) {
                if (i >= numColumns) {
                    i = 0;
                }
                if (i == 0) {
                    hb = new HBox();
                    hb.setAlignment(Pos.CENTER);
                    vb.getChildren().add(hb);
                }

                // filtering
                if (filter != null
                        && !b.getBook().getName().toLowerCase().contains(filter.toLowerCase())
                        && !b.getBook().getDescription().toLowerCase().contains(filter.toLowerCase()))
                    continue;

                b.setOnMouseClicked((event -> {
                    Book book = b.getBook();
                    // bookInfo should accept Book object, not just its parameters!

                    BookInfo.spawnWindow(book, 600, 300, b.getImage(), this);
                }));

                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);

                i++;
            }
            // fill with whitespace - ugly hack, but it works ¯\_(ツ)_/¯
            while(i != 0 && i < numColumns) {
                BookSquareWidget b = new BookSquareWidget(null);
                b.setVisible(false);
                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);
                i++;
            }
            setCenter(scrollPane);
        }
    }

    private int getMaxColumns(int width) {
        return width / 230; // width + margin
    }

    public List<BookSquareWidget> getBooks() {
        return books;
    }
    public void setBooks(List<BookSquareWidget> books) {
        this.books = books;
    }

}
