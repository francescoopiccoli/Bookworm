package com.Bookworm;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

	private Label currentView = new Label("home");
	
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(_generateTopBar());
        mainPane.setCenter(_generateContent());
        mainPane.setBottom(_generateSidebar());
        var scene = new Scene(mainPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox _generateTopBar() {
		VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
		vBox.setAlignment(Pos.CENTER);
        Text title = new Text("Bookworm");
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, 28));
        vBox.getChildren().add(title);

        return vBox;
    }
    
    private Label _generateContent() {
		return currentView;
    }
	
	private HBox _generateSidebar() {
    	HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(8);

        ToggleGroup toggleGroup = new ToggleGroup();
        
        ToggleButton options[] = new ToggleButton[] {
            new ToggleButton("Home"),
            new ToggleButton("Discover"),
            new ToggleButton("Rankings"),
            new ToggleButton("Lists"),
            new ToggleButton("Profile")};

        String defaultView = "Home";
        for (int i=0; i<options.length; i++) {
        	if(defaultView.equals(options[i].getText()))
        		options[i].setSelected(true);
        		
            hBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            hBox.getChildren().add(options[i]);
            options[i].setOnAction((event) -> currentView.setText("viewing "+((ToggleButton) event.getSource()).getText()));
            options[i].setToggleGroup(toggleGroup);
        }

        return hBox;
    }
	

    public static void main(String[] args) {
        launch();
    }

}