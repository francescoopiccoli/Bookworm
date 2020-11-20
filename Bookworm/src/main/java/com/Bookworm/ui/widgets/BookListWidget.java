package com.Bookworm.ui.widgets;

import com.Bookworm.model.Book;
import com.Bookworm.ui.views.BookInfoView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookListWidget extends ScrollPane {
    private List<BookWidget> books;
    private Image image;
    private ImageView imageView;
    private String currentFilter = "";
    // by default the spawned book list will update this window, but it can be changed
    private BookListWidget parentWidget = this;
    public static final String PLACEHOLDER_IMAGE_URI = "/Images/owl_placeholder.png";


    public BookListWidget getParentWidget() {
        return parentWidget;
    }

    public void setParentWidget(BookListWidget parentWidget) {
        this.parentWidget = parentWidget;
    }

    public BookListWidget(List<Book> books) throws NullPointerException {
        super();
        this.books = new ArrayList<>();
        for (Book book : books) {
            // we could even maybe async-ize this thing as well?
            this.books.add(new BookWidget(book));
        }

        setFitToHeight(true);
        setFitToWidth(true);

        widthProperty().addListener((e) -> updateList(null));
        updateList();

    }

    public void updateList() throws NullPointerException {
        updateList("");
    }

    public void updateList(String filter) throws NullPointerException {
        if (filter != null)
            currentFilter = filter;

        if (books == null || books.size() == 0) {

            image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
            imageView = new ImageView(this.image);
            imageView.setFitHeight(250);
            imageView.setFitWidth(500);
            VBox centerVBox = new VBox();
            HBox hBox = new HBox();
            centerVBox.getChildren().addAll(hBox, imageView);
            centerVBox.setSpacing(50);
            centerVBox.setAlignment(Pos.BASELINE_CENTER);
            setContent(centerVBox);
            try {
                image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
                imageView.setImage(image);
            } catch (IllegalArgumentException | NullPointerException e) {
                image = new Image(getClass().getResource(BookListWidget.PLACEHOLDER_IMAGE_URI).toExternalForm());
            }

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

                    try {
                        BookInfoView.spawnWindow(book, b.getImage(), parentWidget);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

                HBox.setMargin(b, new Insets(20));
                hb.getChildren().add(b);

                i++;
            }

            // fill with whitespace
            while (i != 0 && i < numColumns) {
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
        for (Book book : books) {
            // we could even maybe async-ize this thing as well?
            this.books.add(new BookWidget(book));
        }
    }

    public Image getImage() {
        return image;
    }

}
