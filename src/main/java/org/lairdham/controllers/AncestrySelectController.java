package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.lairdham.App;
import org.lairdham.models.Ancestry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AncestrySelectController {

    @FXML
    ListView<String> ancestryList;

    @FXML
    Text ancestryDescription;

    Map<String, Ancestry> ancestryDataMap = new HashMap<>();

    @FXML
    protected void initialize() {

        try {
            ancestryDataMap = new ObjectMapper().readValue(App.class.getResource("datafiles/ancestrydata.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            System.out.println("Error when trying to load ancestrydata.json");
        }

        if (ancestryDataMap != null && !ancestryDataMap.isEmpty()) {
            ancestryDataMap.keySet().forEach(ancestryName -> ancestryList.getItems().add(ancestryName));
        }
    }

    @FXML
    private void nextPage() throws IOException {
        App.setRoot("traits");
    }

    @FXML
    private void selectAncestry() {
        Ancestry selectedAncestry = ancestryDataMap.get(ancestryList.getSelectionModel().getSelectedItem());
        ancestryDescription.setText(selectedAncestry.getDescription());
    }
}