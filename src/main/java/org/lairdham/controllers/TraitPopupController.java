package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.lairdham.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TraitPopupController {

    @FXML
    public TitledPane titledPane;
    @FXML
    public Text description;

    private Stage stage;
    HashMap<String, List<String>> traitDescriptions = new HashMap<>();

    @FXML
    protected void initialize() {
        try {
            traitDescriptions = new ObjectMapper().readValue(App.class.getResource("datafiles/traitDescriptions.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            System.out.println("Error trying to load traitDescriptions.json");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        String traitTitle = (String) this.stage.getUserData();
        titledPane.setText(traitTitle + " " + traitDescriptions.get(traitTitle).get(0));
        description.setText(traitDescriptions.get(traitTitle).get(1));
        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE)
            {
                System.out.println("click on escape");
                stage.close();
            }
        });
    }
}
