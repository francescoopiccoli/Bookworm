package com.Bookworm.ui.views;

import com.Bookworm.controller.GoogleBooksClient;
import com.Bookworm.model.Book;
import com.Bookworm.ui.widgets.BookListWidget;
import com.Bookworm.ui.widgets.BookWidget;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.LinkedList;
import java.util.List;

// !!! to fix  input with return null (no results found)

public class DiscoverView extends BorderPane {

    BorderPane layout;
    BookListWidget bookListWidget;
    // sicuro che sia static?
    public static List<Book> bookList = new LinkedList<>();
    private boolean loadingStatus;
    private TextField searchWidget;
    private Label searchPlaceholder;

    public static List<Book> getbookList() {
        return bookList;
    }

    public static void setbookList(List<Book> bookList) {
        DiscoverView.bookList = bookList;
    }

    private void setLoadingStatus(boolean loadingStatus) {
        this.loadingStatus = loadingStatus;
        // ugly af - better have some kind of loading widget
        searchWidget.setVisible(!loadingStatus);
        searchPlaceholder.setVisible(loadingStatus);
    }
    private boolean getLoadingStatus() {
        return loadingStatus;
    }



    public DiscoverView() {
        //Create an instance of Discover to fill the borderpane with its functions
        setTop(createTopDisc());
        setCenter(getCenterDisc());

    }

    public Node getCenterDisc() {
        if(bookListWidget == null)
            bookListWidget = new BookListWidget(bookList);

        bookListWidget.setBooks(bookList);

        bookListWidget.updateList();

        return bookListWidget;

    }

    public Node createTopDisc() {
        VBox vb = new VBox();
        HBox hb = new HBox();
        searchWidget = new TextField();
        searchWidget.setOnAction(event -> {
            if (!searchWidget.getText().equals("")) {
                RefreshThread thread = new RefreshThread(this, searchWidget.getText());
                thread.start();
            }
        });

        searchPlaceholder = new Label("Search in progress...");
        searchPlaceholder.setVisible(false);

        vb.getChildren().addAll(searchWidget,searchPlaceholder,hb);
        vb.setSpacing(5.5);
        return  vb;
    }
    public void refresh() {
        setCenter(getCenterDisc());
    }

    private class RefreshThread extends Thread {
        private DiscoverView d;
        private String query;
        public RefreshThread(DiscoverView d, String query) {
            this.d = d;
            this.query = query;
        }
        public void run() {
            if(d.getLoadingStatus())
                return; // don't mess with multiple searches at once
            setLoading(true);

            List<Book> bookList = GoogleBooksClient.searchBooks(query);
            if(bookList != null && !bookList.isEmpty()){
                d.setbookList(bookList);
                Platform.runLater(() -> d.setCenter(d.getCenterDisc()));
            }
            setLoading(false);
        }

        private void setLoading(boolean b) {
            Platform.runLater(() -> d.setLoadingStatus(b));
        }
    }


}
