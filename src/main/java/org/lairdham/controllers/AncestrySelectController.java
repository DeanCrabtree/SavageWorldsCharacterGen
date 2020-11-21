package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.lairdham.App;
import org.lairdham.models.Ancestry;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AncestrySelectController {

    @FXML
    ListView<String> ancestryList;

    @FXML
    Text ancestryDescription;

    @FXML
    Button nextButton;

    Map<String, Ancestry> ancestryDataMap = new HashMap<>();

    Character characterInProgress;

    @FXML
    protected void initialize() {

        characterInProgress = CharacterCreatorSingleton.getInstance().getCharacter();

        try {
            ancestryDataMap = new ObjectMapper().readValue(App.class.getResource("datafiles/ancestrydata.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            System.out.println("Error when trying to load ancestrydata.json");
        }

        if (ancestryDataMap != null && !ancestryDataMap.isEmpty()) {
            ancestryDataMap.keySet().forEach(ancestryName -> ancestryList.getItems().add(ancestryName));
        }

        if (characterInProgress.getAncestry() != null) {
            ancestryList.getSelectionModel().select(characterInProgress.getAncestry().getName());
            selectAncestry();
        }
    }

    @FXML
    private void nextPage() throws IOException {
        characterInProgress.setAncestry(ancestryDataMap.get(ancestryList.getSelectionModel().getSelectedItem()));
        CharacterCreatorSingleton.getInstance().setCharacter(characterInProgress);
        App.setRoot("hindrances");
    }

    @FXML
    private void selectAncestry() {
        Ancestry selectedAncestry = ancestryDataMap.get(ancestryList.getSelectionModel().getSelectedItem());
        if (selectedAncestry != null) {
            ancestryDescription.setText(selectedAncestry.getDescription());
            nextButton.setDisable(false);
        }
    }
}