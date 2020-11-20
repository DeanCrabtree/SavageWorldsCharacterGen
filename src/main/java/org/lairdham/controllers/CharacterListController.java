package org.lairdham.controllers;

import javafx.fxml.FXML;
import org.lairdham.App;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;

import java.io.IOException;

public class CharacterListController {

    @FXML
    private void createNewCharacter() throws IOException {
        CharacterCreatorSingleton.getInstance().setCharacter(new Character());
        App.setRoot("ancestrySelect");
    }
}
