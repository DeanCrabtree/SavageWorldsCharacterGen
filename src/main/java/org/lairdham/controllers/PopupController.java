package org.lairdham.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class PopupController {

    @FXML
    public TitledPane titledPane;
    @FXML
    public Text description;

    public void setData(String title, String body) {
        setData(title, body, null);
    }

    public void setData(String title, String body, TextAlignment bodyAlignment) {
        titledPane.setText(title);
        description.setText(body);
        if (bodyAlignment != null){
            description.setTextAlignment(bodyAlignment);
        }
    }
}
