package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lairdham.App;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.spc.SuperPower;
import org.lairdham.models.spc.SuperPowerModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperPowersController {

    @FXML
    public Button chooseModifierButton;
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
    @FXML
    TableView<SuperPower> selectedPowersTableView;
    @FXML
    Label powerPointsCounterLabel;

    Map<String, SuperPower> powersMap = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        powerPointsCounterLabel.setText("Power Points: " + characterCreatorSingleton.getCurrentPowerPoints());
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
            superPowerDescriptionTitle.setText(superPower.getName() + " (" + superPower.getCostAsString() + ")");
            trappingsText.setText("Trappings: " + superPower.getTrappings());
            superPowerDescription.setText(superPower.getDescription());
            chooseModifierButton.setVisible(true);
        }

    }

    @FXML
    public void selectPower() {
    }

    @FXML
    public void viewSelectedPower() {
    }

    @FXML
    public void chooseModifier() throws IOException {
        SuperPower power = powersMap.get(powersListView.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle(power.getName() + " Modifiers");

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/spc/chooseSuperPowerModifier.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if(t.getCode()== KeyCode.ESCAPE) {
                stage.close();
            }
        });

        ChooseSuperPowerModifierController controller = fxmlLoader.getController();
        controller.setSuperPower(power);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void loadSuperPowers() {
        try {
            List<SuperPower> superPowersList = objectMapper.readValue(App.class.getResource("datafiles/spc/superPowers.json"), new TypeReference<>() {});
            superPowersList.forEach(superPower -> powersMap.put(superPower.getName(), superPower));
        } catch (IOException e) {
            System.out.println("Error when trying to load superPowers.json");
        }
    }
}
