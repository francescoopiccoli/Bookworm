package com.Bookworm;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * JavaFX App
 */
public class App extends Application {

	private Region currentView = new DiscoverView();
	private Map<String,Region> views = new LinkedHashMap<>();
	private BorderPane mainPane;

    // array to keep track of all the book info views, to avoid opening two views of the same book
    public static ArrayList<Book> openedBooks = new ArrayList<>();

    // does not work idk why - TO TEST
    public static boolean hasOpenedBook(Book book, ArrayList<Book> openedBooks) {
        for (int i = 0; i < openedBooks.size(); i++){
            System.out.println(openedBooks.get(i).toString());
            if (openedBooks.get(i).getName() == book.getName() && openedBooks.get(i).getAuthor() == book.getAuthor()) {
                System.out.println("WAS PRESENT");
                return true;
            }
        }
        return false;
    }

    @Override
    public void start(Stage stage) {
        //views.put("Home", new Label("implement me"));
        DiscoverView discoverView = new DiscoverView();
        //BookListWidget readingListWidget;
        views.put("Discover", discoverView);
        try {
            BookListView readingListView = new BookListView("Reading List", DatabaseManager.getInstance().getBooks());
            views.put("My Books", readingListView);
            discoverView.setMyBooksWidget(readingListView.getListWidget());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        views.put("Bookshelves", new BookshelfView());
        //views.put("Settings", new DiscoverView()); -> reset library ; about / info ; ...?

        mainPane = new BorderPane();
        mainPane.setTop(_generateTopBar());
        mainPane.setCenter(_generateContent(""));
        mainPane.setBottom(_generateSidebar());
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
        title.setFont(Font.font("Noto Sans Condensed", FontWeight.BOLD, 28));
        vBox.getChildren().add(title);

        return vBox;
    }
    
    private Region _generateContent(String name) {
        for (Map.Entry<String, Region> entry : views.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }
        // default
        return new Label("Hi! Select a view to start");
    }
	
	private HBox _generateSidebar() {
    	HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(8);

        ToggleGroup toggleGroup = new ToggleGroup();


        for (Map.Entry<String, Region> entry : views.entrySet()) {
            ToggleButton button = new NavToggleButton(entry.getKey());
            if (currentView.equals(entry.getValue()))
                button.setSelected(true);

            HBox.setMargin(button, new Insets(0, 0, 0, 8));
            hBox.getChildren().add(button);
            button.setOnAction((event) -> {
                String text = ((ToggleButton) event.getSource()).getText();
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