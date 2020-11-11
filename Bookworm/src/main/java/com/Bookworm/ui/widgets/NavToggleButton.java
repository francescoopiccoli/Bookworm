package com.Bookworm.ui.widgets;

import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;

public class NavToggleButton extends ToggleButton {
    public NavToggleButton(String text) {
        super(text);

        setPrefWidth(100);
        setPadding(new Insets(5));

        //setStyle("-fx-background-color: linear-gradient(to top, #ccc, #fff)");
    }
}
