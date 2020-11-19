package org.lairdham;

import java.io.IOException;
import javafx.fxml.FXML;

public class CharacterListController {

    @FXML
    private void createNewCharacter() throws IOException {
        App.setRoot("ancestrySelect");
    }
}
