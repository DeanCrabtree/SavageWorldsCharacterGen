package org.lairdham.controllers;

import javafx.fxml.FXML;
import org.lairdham.App;

import java.io.IOException;

public class HindrancesController {

    @FXML
    private void nextPage() throws IOException {
        App.setRoot("traits");
    }

    @FXML
    private void prevPage() throws IOException {
        App.setRoot("ancestrySelect");
    }

    @FXML
    public void selectHindrance() {

    }
}
