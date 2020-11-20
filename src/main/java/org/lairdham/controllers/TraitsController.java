package org.lairdham.controllers;

import javafx.fxml.FXML;
import org.lairdham.models.CharacterCreatorSingleton;

public class TraitsController {

    @FXML
    public void clickButton() {

        System.out.println(CharacterCreatorSingleton.getInstance().getCharacter());
        System.out.println("something");
    }
}
