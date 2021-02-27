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
        System.out.println(superPower.getName() + " " + superPower.getTotalPowerCost());
        characterCreatorSingleton.adjustCurrentPowerPoints(-superPower.getTotalPowerCost());
        selectedPowersTableView.getItems().add(superPower);
        powersMap.put(superPower.getName(), new SuperPower(superPower));
        resizeSelectedPowersTable();
        powerPointsCounterLabel.setText("Power Points: " + characterCreatorSingleton.getCurrentPowerPoints());
        viewPower();
    }

    @FXML
    public void viewSelectedPower() {
        if (selectedPowersTableView.getSelectionModel().getSelectedItem() != null) {
            removeSelectedPowerButton.setDisable(false);
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
    }
    private void setColumnWidthToMatchContent(TableColumn column) {
        Object cellData = column.getCellData(selectedPowersTableView.getItems().size()-1);
        if(cellData != null) {
            Text data = new Text(cellData.toString());
            column.setMinWidth(data.getLayoutBounds().getWidth() + 10.0);
        }
    }
}
