package com.Bookworm;

import com.Bookworm.controller.DatabaseManager;
import com.Bookworm.model.Book;
import com.Bookworm.ui.Booklist;
import com.Bookworm.ui.Discover;
import com.Bookworm.ui.BookListView;
import com.Bookworm.ui.NavToggleButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;


/**
 * JavaFX App
 */
public class App extends Application {

	private Region currentView = new Discover();
	private Map<String,Region> views = new LinkedHashMap<>();
	private BorderPane mainPane;
	
    @Override
    public void start(Stage stage) {

        ArrayList<Book> demoList = new ArrayList<>();
        demoList.add(new Book("A cute lil' book", "The Great Gatsby", null, "Francis Scott Fitzgerald", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green"));
        demoList.add(new Book("A thicc big book", "War and Peace", null, "Lev Tolstoj", 5, null, "https://cataas.com/cat/says/meow?size=50&color=green"));
        demoList.add(new Book("A damn epic book", "The Lord of the Rings", null, "J. R. R. Tolkien", 5,null, "https://cataas.com/cat/says/meow?size=50&color=green"));
        demoList.add(new Book("A cute lil' book", "The Great Gatsby", null, "Francis Scott Fitzgerald", 5,null, "https://c8.alamy.com/comp/F1WJN3/full-moon-harvest-moon-large-file-size-from-the-archives-of-press-F1WJN3.jpg"));
        demoList.add(new Book("A thicc big book", "War and Peace", null, "Lev Tolstoj", 5,null, ""));
        demoList.add(new Book("A damn epic book", "The Lord of the Rings", null, "J. R. R. Tolkien", 5,null, ""));
        demoList.add(new Book("A thicc big book", "War and Peace", null, "Lev Tolstoj", 5, null,""));
        demoList.add(new Book("A damn epic book", "The Lord of the Rings", null, "J. R. R. Tolkien", 5,null, ""));
        demoList.add(new Book("A cute lil' book", "The Great Gatsby", null, "Francis Scott Fitzgerald", 5,null, ""));

        views.put("Home", new Label("implement me"));
        views.put("Discover", new Discover());
        try {
            views.put("My Books", new BookListView("Reading List", DatabaseManager.getInstance().getBooks()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        views.put("Booklist", new Booklist());
        views.put("Settings", new Discover());

        //var javaVersion = SystemInfo.javaVersion();
        //var javafxVersion = SystemInfo.javafxVersion();

        mainPane = new BorderPane();
        mainPane.setTop(_generateTopBar());
        mainPane.setCenter(_generateContent(""));
        mainPane.setBottom(_generateSidebar());
        var scene = new Scene(mainPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Stylesheets/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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