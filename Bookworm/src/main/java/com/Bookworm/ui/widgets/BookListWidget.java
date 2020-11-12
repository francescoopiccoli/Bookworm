package com.Bookworm.ui.widgets;

import com.Bookworm.model.Book;
import com.Bookworm.ui.views.BookInfoView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BookListWidget extends ScrollPane {
    private List<BookWidget> books;
    private String currentFilter = "";

    public BookListWidget(List<Book> books) {
        super();
        this.books = new ArrayList<>();
        for(Book book : books) {
            // we could even maybe async-ize this thing as well?
            this.books.add(new BookWidget(book));
        }

        setFitToHeight(true);
        setFitToWidth(true);

        widthProperty().addListener((e) ->  updateList(null) );
        updateList();

    }

    public void updateList() {
        updateList("");
    }
    public void updateList(String filter) {
        if(filter != null)
            currentFilter = filter;

        if (books == null || books.size() == 0) {
            setContent(new Label("This list feels so empty :("));
        } else {
            VBox vb = new VBox();
            vb.setAlignment(Pos.CENTER);

            int width = (int) getWidth();
            int numColumns = getMaxColumns(width);

            int i = 0;
            HBox hb = new HBox();
            for (BookWidget b : books) {
                System.out.println(b.book.getName());
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
                        && (
                            b.getBook().getDescription() == null ||
                            !b.getBook().getDescription().toLowerCase().contains(filter.toLowerCase())
                        )
                )
                    continue;

                b.setOnMouseClicked((event -> {
                    Book book = b.getBook();
                    // bookInfo should accept Book object, not just its parameters!

                    BookInfoView.spawnWindow(book, 600, 300, b.getImage(), this);
                }));

                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);

                i++;
            }
            // fill with whitespace - ugly hack, but it works ¯\_(ツ)_/¯
            while(i != 0 && i < numColumns) {
                BookWidget b = new BookWidget(null);
                b.setVisible(false);
                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);
                i++;
            }

            setContent(vb);
        }
    }

    private int getMaxColumns(int width) {
        return width / 230; // width + margin
    }

    public List<BookWidget> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = new ArrayList<>();
        for(Book book : books) {
            // we could even maybe async-ize this thing as well?
            this.books.add(new BookWidget(book));
        }
    }
}