package com.Bookworm;

import com.Bookworm.ui.Disc.Discover;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * JavaFX App
 */
public class App extends Application {

	private Region currentView = new Discover();
	private Map<String,Region> views = new HashMap<>();
	private BorderPane mainPane;
	
    @Override
    public void start(Stage stage) {

        views.put("Home", new Label("implement me"));
        views.put("Discover", new Discover());
        views.put("Rankings", new Button("lol"));
        views.put("Lists", new Discover());
        views.put("Settings", new Discover());

        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

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
        Iterator<Map.Entry<String, Region>> iterator = views.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Region> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());

            if(entry.getKey().equals(name)) {
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


        Iterator<Map.Entry<String, Region>> iterator = views.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Region> entry = iterator.next();

            ToggleButton button = new ToggleButton(entry.getKey());
        	if(currentView.equals(entry.getValue()))
        		button.setSelected(true);
        		
            hBox.setMargin(button, new Insets(0, 0, 0, 8));
            hBox.getChildren().add(button);
            button.setOnAction((event) -> {
                mainPane.setCenter(_generateContent(
                        ((ToggleButton) event.getSource()).getText()
                ));
            });
            button.setToggleGroup(toggleGroup);
        }

        return hBox;
    }
	

    public static void main(String[] args) {
        launch();
    }

}