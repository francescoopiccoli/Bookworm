package com.Bookworm;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.model.Bookshelf;
import com.Bookworm.ui.views.BookListView;
import com.Bookworm.ui.views.BookshelfView;
import com.Bookworm.ui.views.DiscoverView;
import com.Bookworm.ui.widgets.NavToggleButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * JavaFX App
 */
public class App extends Application {

	private Region currentView = new DiscoverView();
	private Map<String,Region> views = new LinkedHashMap<>();
	private BorderPane mainPane;
	private ToggleGroup toggleGroup;
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    // array to keep track of all the book info views, to avoid opening two views of the same book
    public static List<Book> openedBooks = new ArrayList<>();

    public static boolean hasOpenedBook(Book book) {
        if (openedBooks.size() > 0) {
        for (Book openedBook : openedBooks) {
            if (book.getId() > 0 && openedBook.getId() == book.getId()) {
                //System.out.println("Book opened, present with id");
                return true;
            } else if (openedBook.getName().equals(book.getName()) && openedBook.getAuthor().equals(book.getAuthor())) {
                //System.out.println("Book opened, present with name and author");
                return true;
            }
        }
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        //creating default bookshelf if not present
        try {
            if (DatabaseManager.getInstance().getBookshelfID("Default") == -1) {
                Bookshelf defaultB = new Bookshelf("Default", "Default bookshelf");
                DatabaseManager.getInstance().insertBookshelf(defaultB);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DiscoverView discoverView = new DiscoverView();
        views.put("Discover", discoverView);
        try {
            BookListView readingListView = new BookListView("Reading List", DatabaseManager.getInstance().getBooks());
            views.put("My Books", readingListView);
            discoverView.setMyBooksWidget(readingListView.getListWidget());
        } catch (SQLException | ClassNotFoundException throwables) {
            LOGGER.log( Level.SEVERE, throwables.toString(), throwables);
            //throwables.printStackTrace();
        }
        views.put("Bookshelves", new BookshelfView());

        mainPane = new BorderPane();
        mainPane.setTop(_generateTopBar());
        mainPane.setBottom(_generateSidebar());
        mainPane.setCenter(_generateContent("Discover"));
        var scene = new Scene(mainPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Stylesheets/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        // when you close the main app you close also all the other books
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }



    private VBox _generateTopBar() {
		VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
		vBox.setAlignment(Pos.CENTER);
        Text title = new Text("Bookworm");
        title.setFill(Paint.valueOf("#F2BC94"));
        title.setFont(Font.font("Noto Sans Condensed", FontWeight.BOLD, 28));
        vBox.getChildren().add(title);

        return vBox;
    }
    
    private Region _generateContent(String name) {
        for (Map.Entry<String, Region> entry : views.entrySet()) {
            if (entry.getKey().equals(name)) {
                selectButton(name);
                return entry.getValue();
            }
        }
        // default
        return new Label("View not found");
    }

    private void selectButton(String name) {
        if(toggleGroup != null) {
            for (Toggle toggle : toggleGroup.getToggles()) {
                ToggleButton button = (ToggleButton) toggle; // we assume all to be buttons
                if (button.getText().equals(name) && !button.isSelected())
                    button.setSelected(true);
            }
        }
    }
	
	private HBox _generateSidebar() {
    	HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(8);

        toggleGroup = new ToggleGroup();


        for (Map.Entry<String, Region> entry : views.entrySet()) {
            ToggleButton button = new NavToggleButton(entry.getKey());
            button.getStyleClass().add("buttonsBasic");
            if (currentView.equals(entry.getValue()))
                button.setSelected(true);

            HBox.setMargin(button, new Insets(0, 0, 0, 8));
            hBox.getChildren().add(button);
            button.setOnAction((event) -> {
                String text = ((ToggleButton) event.getSource()).getText();
                if (text.equals("My Books")) {
                    BookListView readingListView = null;
                    try {
                        readingListView = new BookListView("Reading List", DatabaseManager.getInstance().getBooks());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    readingListView.getListWidget().updateList();
                    views.put("My Books", readingListView);
                }
                mainPane.setCenter(_generateContent(text));
            });
            button.setToggleGroup(toggleGroup);
        }

        return hBox;
    }
	

    public static void main(String[] args) {
        launch();
    }

}