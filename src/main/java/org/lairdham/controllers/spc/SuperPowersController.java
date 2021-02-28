package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lairdham.App;
import org.lairdham.controllers.spc.ChooseSuperPowerModifierController.DialogType;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.spc.SuperPower;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    TableView<SuperPower> selectedPowersTableView;
    @FXML
    Label powerPointsCounterLabel;
    @FXML
    ToolBar buttonToolbar;
    @FXML
    Pane toolbarPane;
    @FXML
    Button removeSelectedPowerButton;

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
        resizeSelectedPowersTable();
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
            trappingsText.setText("Trappings: " + superPower.getExampleTrappings());
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
            setupToolbarButtons(superPower);
        }

    }

    private void setupToolbarButtons(SuperPower superPower) {
        buttonToolbar.getItems().clear();
        buttonToolbar.getItems().add(0, toolbarPane);

        if (superPower.isLevelled()) {
            Button button = new Button("Select Power: " + setMinValueToOne(superPower.getBaseCost() + superPower.getPointsSpentOn()) + " point(s)");
            button.setOnAction(actionEvent -> {
                superPower.addPointsSpentOn((Integer) button.getUserData());
                superPower.setSelectedLevels(((Integer) button.getUserData())/superPower.getBaseCost());
                selectPower(superPower);
            });
            button.setUserData(superPower.getBaseCost());
            buttonToolbar.getItems().add(1, button);

            Label label = new Label("Level(s):");
            buttonToolbar.getItems().add(1, label);
            Spinner<Integer> levelSpinner = new Spinner<>(1, calculateMaxLevelsAvailable(superPower), 1);
            levelSpinner.setPrefWidth(50.0);
            levelSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                button.setUserData(newValue * superPower.getBaseCost());
                button.setText("Select Power: " + (superPower.getPointsSpentOn() + (Integer) button.getUserData()) + " point(s)");
            });
            if ((Integer) button.getUserData() + superPower.getPointsSpentOn() > (characterCreatorSingleton.getCurrentPowerPoints())) {
                button.setDisable(true);
                levelSpinner.setDisable(true);
            }
            buttonToolbar.getItems().add(2, levelSpinner);

        } else if (superPower.getSteppedCosts()!= null && superPower.getSteppedCosts().length > 1) {
            int counter = 1;
            for (int steppedCost : superPower.getSteppedCosts()) {
                Button button = new Button("Select Power: " + setMinValueToOne(steppedCost + superPower.getPointsSpentOn()) + " point(s)");
                button.setOnAction(actionEvent -> {
                    superPower.addPointsSpentOn(steppedCost);
                    superPower.setSelectedSteppedCost(steppedCost);
                    selectPower(superPower);
                });
                if (steppedCost + superPower.getPointsSpentOn() > (characterCreatorSingleton.getCurrentPowerPoints())) {
                    button.setDisable(true);
                }
                buttonToolbar.getItems().add(counter, button);
                counter++;
            }
        } else {
            Button button = new Button("Select Power: " + setMinValueToOne(superPower.getBaseCost() + superPower.getPointsSpentOn()) + " point(s)");
            button.setOnAction(actionEvent -> {
                superPower.addPointsSpentOn(superPower.getBaseCost());
                selectPower(superPower);
            });
            buttonToolbar.getItems().add(1, button);

            if (superPower.getBaseCost() + superPower.getPointsSpentOn() > (characterCreatorSingleton.getCurrentPowerPoints())) {
                button.setDisable(true);
            }
        }
    }

    private int calculateMaxLevelsAvailable(SuperPower superPower) {
        return Math.max(1,Math.min(characterCreatorSingleton.getCurrentPowerPoints() - superPower.getPointsSpentOn() / superPower.getBaseCost(), superPower.getMaxLevels()));
    }

    public void selectPower(SuperPower superPower) {
        Optional<String> userDefinedTrappings = showTrappingsInputDialog(superPower.getName());

        userDefinedTrappings.ifPresent(trappings -> {
            System.out.println(superPower.getName() + " " + superPower.getTotalPowerCost() + " " + trappings);
            superPower.setUserDefinedTrappings(trappings);
            characterCreatorSingleton.adjustCurrentPowerPoints(-superPower.getTotalPowerCost());
            selectedPowersTableView.getItems().add(superPower);
            powersMap.put(superPower.getName(), new SuperPower(superPower));
            resizeSelectedPowersTable();
            powerPointsCounterLabel.setText("Power Points: " + characterCreatorSingleton.getCurrentPowerPoints());
            viewPower();
        });
    }

    @FXML
    public void viewSelectedPower() {
        SuperPower superPower = selectedPowersTableView.getSelectionModel().getSelectedItem();
        if (superPower != null) {
            removeSelectedPowerButton.setDisable(false);
            superPowerDescriptionTitle.setText(superPower.getName());
            trappingsText.setText("Trappings: " + superPower.getUserDefinedTrappings());
            superPowerDescription.setText(superPower.getDescription());

            StringBuilder chosenModifiersStringBuilder = new StringBuilder();
            if (!superPower.getChosenModifiers().isEmpty()) {
                chosenModifiersStringBuilder.append("Chosen Mods:\n");
                superPower.getChosenModifiers().forEach(modifier -> chosenModifiersStringBuilder.append(modifier.getName())
                        .append(": ").append(modifier.getPointsSpentOn()).append(" point(s)\n"));
            }

            if (superPower.getSteppedCosts()!= null && superPower.getSteppedCosts().length > 1) {
                chosenModifiersStringBuilder.append("\nBase Power Cost: ").append(superPower.getSelectedSteppedCost()).append(" point(s)\n");
            } else {
                chosenModifiersStringBuilder.append("\nBase Power Cost: ").append(superPower.getBaseCost()).append(" point(s)\n");
            }
            if (superPower.isLevelled()) {
                chosenModifiersStringBuilder.append("Levels Taken: ").append(superPower.getSelectedLevels()).append(" (")
                        .append(superPower.getSelectedLevels()).append(" x ").append(superPower.getBaseCost()).append(" = ")
                        .append(superPower.getSelectedLevels()*superPower.getBaseCost()).append(" point(s))\n");
            }
            chosenModifiersStringBuilder.append("\nTotal Power Cost: ").append(superPower.getTotalPowerCost()).append(" point(s)");
            chosenModifiersText.setText(chosenModifiersStringBuilder.toString());
            chooseModifierButton.setVisible(false);
            removeModifierButton.setVisible(false);
            buttonToolbar.getItems().clear();
        }
    }

    @FXML
    public void removeSelectedPower() {
        SuperPower selectedPower = selectedPowersTableView.getSelectionModel().getSelectedItem();
        if (selectedPower != null) {
            selectedPowersTableView.getItems().remove(selectedPower);
            characterCreatorSingleton.adjustCurrentPowerPoints(selectedPower.getTotalPowerCost());
            powerPointsCounterLabel.setText("Power Points: " + characterCreatorSingleton.getCurrentPowerPoints());
        }
        removeSelectedPowerButton.setDisable(true);
        viewPower();
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

    private Optional<String> showTrappingsInputDialog(String superPowerName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(superPowerName + " Trappings");
        dialog.setHeaderText("Trappings describe how a power looks or is acquired, those listed on the Power entry are just examples. Please enter your power's trappings below.\nYou can also include relevant descriptions of modifiers (e.g. type of device, or limitation)");
        dialog.setGraphic(null);

        return dialog.showAndWait();
    }

    private void loadSuperPowers() {
        try {
            List<SuperPower> superPowersList = objectMapper.readValue(App.class.getResource("datafiles/spc/superPowers.json"), new TypeReference<>() {});
            superPowersList.forEach(superPower -> powersMap.put(superPower.getName(), superPower));
        } catch (IOException e) {
            System.out.println("Error when trying to load superPowers.json: " + e.getMessage());
        }
    }

    private int setMinValueToOne(int value) {
        if (value > 0) {
            return value;
        } else {
            return 1;
        }
    }

    private void resizeSelectedPowersTable() {
        ObservableList<TableColumn<SuperPower, ?>> columns = selectedPowersTableView.getColumns();
        setColumnWidthToMatchContent(columns.get(2));
        setColumnWidthToMatchContent(columns.get(3));
        setColumnWidthToMatchContent(columns.get(4));
    }
    private void setColumnWidthToMatchContent(TableColumn column) {
        Object cellData = column.getCellData(selectedPowersTableView.getItems().size()-1);
        if(cellData != null) {
            Text data = new Text(cellData.toString());
            column.setMinWidth(data.getLayoutBounds().getWidth() + 10.0);
        }
    }
}
