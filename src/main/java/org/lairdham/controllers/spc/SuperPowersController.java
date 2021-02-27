package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
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
import org.lairdham.controllers.spc.ChooseSuperPowerModifierController.DialogType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperPowersController {

    @FXML
    public Button chooseModifierButton;
    @FXML
    public Button removeModifierButton;
    @FXML
    ListView<String> powersListView;
    @FXML
    Label superPowerDescriptionTitle;
    @FXML
    Text trappingsText;
    @FXML
    Text superPowerDescription;
    @FXML
    Text chosenModifiersText;
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

            if (!superPower.getChosenModifiers().isEmpty()) {
                StringBuilder chosenModifiersStringBuilder = new StringBuilder();
                chosenModifiersStringBuilder.append("Chosen Mods:\n");
                superPower.getChosenModifiers().forEach(modifier -> chosenModifiersStringBuilder.append(modifier.getName())
                        .append(": ").append(modifier.getPointsSpentOn()).append(" point(s)\n"));
                chosenModifiersText.setText(chosenModifiersStringBuilder.toString());
                removeModifierButton.setDisable(false);
            } else {
                chosenModifiersText.setText("");
                removeModifierButton.setDisable(true);
            }
            chooseModifierButton.setVisible(true);
            removeModifierButton.setVisible(true);
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
        ChooseSuperPowerModifierController controller = createModifierPopupWindow(power, DialogType.CHOOSE);

        if (controller.getChosenModifier() != null) {
            power.addChosenModifier(controller.getChosenModifier());
            power.addPointsSpentOn(controller.getChosenModifier().getPointsSpentOn());

            powersMap.put(power.getName(), power);
            viewPower();
            System.out.println(controller.getChosenModifier() + " cost: " + controller.getChosenModifier().getPointsSpentOn());
        }

    }

    @FXML
    public void removeModifier() throws IOException {
        SuperPower power = powersMap.get(powersListView.getSelectionModel().getSelectedItem());
        ChooseSuperPowerModifierController controller = createModifierPopupWindow(power, DialogType.REMOVE);

        if (controller.getChosenModifier() != null) {
            power.removeChosenModifier(controller.getChosenModifier());
            power.subtractPointsSpentOn(controller.getChosenModifier().getPointsSpentOn());
            powersMap.put(power.getName(), power);
            viewPower();
        }

    }

    private ChooseSuperPowerModifierController createModifierPopupWindow(SuperPower power, DialogType dialogType) throws IOException {
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
        controller.setUp(power, dialogType);
        stage.setScene(scene);
        stage.showAndWait();
        return controller;
    }

    private void loadSuperPowers() {
        try {
            List<SuperPower> superPowersList = objectMapper.readValue(App.class.getResource("datafiles/spc/superPowers.json"), new TypeReference<>() {});
            superPowersList.forEach(superPower -> powersMap.put(superPower.getName(), superPower));
        } catch (IOException e) {
            System.out.println("Error when trying to load superPowers.json: " + e.getMessage());
        }
    }
}
