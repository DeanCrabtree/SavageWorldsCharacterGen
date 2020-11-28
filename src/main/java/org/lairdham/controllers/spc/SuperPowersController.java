package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.lairdham.App;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.spc.SuperPower;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperPowersController {

    @FXML
    ListView<String> powersListView;
    @FXML
    Label superPowerDescriptionTitle;
    @FXML
    Text trappingsText;
    @FXML
    Text superPowerDescription;
    @FXML
    Button selectPowerButton;

    Map<String, SuperPower> powersMap = new HashMap<>();

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        loadSuperPowers();
        powersListView.getItems().addAll(powersMap.keySet());
    }

    @FXML
    public void nextPage() {

    }

    @FXML
    public void prevPage() throws IOException {
        App.setRoot("edges");
        characterCreatorSingleton.setCharacter(characterInProgress);
    }

    @FXML
    public void viewPower() {
        SuperPower superPower = powersMap.get(powersListView.getSelectionModel().getSelectedItem());
        if (superPower != null) {
            String costString = "";
            if (superPower.isLevelled()) {
                costString = superPower.getCost() + "/Level";
            } else if (superPower.getSteppedCosts() != null && superPower.getSteppedCosts().length > 0) {
                for (int steppedCost : superPower.getSteppedCosts()) {
                    costString += steppedCost;
                    costString += "|";
                }
                costString = costString.substring(0, costString.length()-1);
            } else {
                costString = ""+superPower.getCost();
            }
            superPowerDescriptionTitle.setText(superPower.getName() + " (" + costString + ")");
            trappingsText.setText("Trappings: " + superPower.getTrappings());
            superPowerDescription.setText(superPower.getDescription());
        }

    }

    @FXML
    public void selectPower() {
    }

    private void loadSuperPowers() {
        try {
            List<SuperPower> superPowersList = new ObjectMapper().readValue(App.class.getResource("datafiles/spc/superPowers.json"), new TypeReference<>() {});
            superPowersList.forEach(superPower -> powersMap.put(superPower.getName(), superPower));
        } catch (IOException e) {
            System.out.println("Error when trying to load superPowers.json");
        }
    }
}
