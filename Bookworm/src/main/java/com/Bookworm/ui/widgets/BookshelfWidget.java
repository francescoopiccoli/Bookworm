package com.Bookworm.ui.widgets;

import com.Bookworm.model.Book;
import com.Bookworm.ui.views.BookListView;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;
import java.util.List;
//NOT USED
public class BookshelfWidget extends BorderPane {
    public BookshelfWidget(String bookshelfTitle,List<Book>books){
        BorderPane pane = new BorderPane();
        BookListView blv = new BookListView(bookshelfTitle,books);
        pane.setCenter(blv);
    }

}
