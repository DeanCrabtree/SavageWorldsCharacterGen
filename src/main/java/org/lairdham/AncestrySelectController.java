package org.lairdham;

import java.io.IOException;
import javafx.fxml.FXML;

public class AncestrySelectController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}