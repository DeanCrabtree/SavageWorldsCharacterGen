package org.lairdham.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

public class PopupController {

    @FXML
    public TitledPane titledPane;
    @FXML
    public Text description;

    public void setStage(Stage stage) {
        HashMap<String, String> displayInfo = (HashMap<String, String>) stage.getUserData();
        titledPane.setText(displayInfo.get("Title"));
        description.setText(displayInfo.get("Description"));

        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE)
            {
                System.out.println("click on escape");
                stage.close();
            }
        });
    }
}
