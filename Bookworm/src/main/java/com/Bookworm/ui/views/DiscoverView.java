package com.Bookworm.ui.views;

import com.Bookworm.controller.GoogleBooksClient;
import com.Bookworm.model.Book;
import com.Bookworm.ui.widgets.BookListWidget;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.LinkedList;
import java.util.List;

// !!! to fix  input with return null (no results found)

public class DiscoverView extends BorderPane {

    BorderPane layout;
    BookListWidget bookListWidget; // the widget from the other page

    public BookListWidget getMyBooksWidget() {
        return myBooksWidget;
    }

    public void setMyBooksWidget(BookListWidget myBooksWidget) {
        this.myBooksWidget = myBooksWidget;
    }

    BookListWidget myBooksWidget;
    // sicuro che sia static?
    public static List<Book> bookList = new LinkedList<>();
    private boolean loadingStatus;
    private TextField searchWidget;

    public static List<Book> getBookList() {
        return bookList;
    }

    public static void setBookList(List<Book> bookList) {
        DiscoverView.bookList = bookList;
    }

    private void setLoadingStatus(boolean loadingStatus) throws NullPointerException {
        this.loadingStatus = loadingStatus;

    }

    private boolean getLoadingStatus() {
        return loadingStatus;
    }

    public DiscoverView() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTopDisc());
        setCenter(getCenterDisc());

    }

    public Node getCenterDisc() throws  NullPointerException{
        if (bookListWidget == null)
            bookListWidget = new BookListWidget(bookList);

        bookListWidget.setBooks(bookList);
        // don't update the Discover list, but the My Books one by default
        bookListWidget.setParentWidget(myBooksWidget);

        bookListWidget.updateList();

        return bookListWidget;

    }

    public Node createTopDisc() {
        VBox vb = new VBox();
        HBox hb = new HBox();

        //create group labels for text

        Label title = new Label();
        title.setText("Discover new books");
        title.setStyle("-fx-font-size: 16;-fx-font-weight:bold");
        Label descriptionText = new Label();
        descriptionText.setText("Search for books and add them to your libraries");
        vb.getChildren().addAll(title,descriptionText);
        vb.setSpacing(1);
        vb.setAlignment(Pos.TOP_LEFT);
        //search box

        searchWidget = new TextField();
        searchWidget.setEditable(true);
        searchWidget.setPrefColumnCount(10);
        searchWidget.setOnAction(event -> {
            if (!searchWidget.getText().equals("")) {
                RefreshThread thread = new RefreshThread(this, searchWidget.getText());
                thread.start();
            }
        });

        //hbox for spacing

        HBox spaceBox = new HBox();
        HBox.setHgrow(spaceBox,Priority.ALWAYS);


        hb.getChildren().addAll(vb,spaceBox,searchWidget);


        return hb;
    }

    public void refresh() {
        setCenter(getCenterDisc());
    }

    // a support class for parallel processing of queries, in order not to clog the UI thread during search operation
    private class RefreshThread extends Thread {
        private DiscoverView d;
        private String query;

        public RefreshThread(DiscoverView d, String query) {
            this.d = d;
            this.query = query;
        }

        public void run() throws NullPointerException {
            if (d.getLoadingStatus())
                return; // don't mess with multiple searches at once
            setLoading(true);

            List<Book> bookList = GoogleBooksClient.searchBooks(query);
            if (bookList != null && !bookList.isEmpty()) {
                d.setBookList(bookList);
                Platform.runLater(() -> d.setCenter(d.getCenterDisc()));
            }
            setLoading(false);
        }

        private void setLoading(boolean b) {
            Platform.runLater(() -> d.setLoadingStatus(b));
        }
    }


}
