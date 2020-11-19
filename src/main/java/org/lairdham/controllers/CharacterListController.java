package org.lairdham.controllers;

import javafx.fxml.FXML;
import org.lairdham.App;

import java.io.IOException;

public class CharacterListController {

    @FXML
    private void createNewCharacter() throws IOException {
        App.setRoot("ancestrySelect");
    }
}
