package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.lairdham.App;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.spc.SuperPower;
import org.lairdham.models.spc.SuperPowerModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseSuperPowerModifierController {

    @FXML
    TableView<SuperPowerModifier> modifiersTableView;
    @FXML
    Label modifierName;
    @FXML
    Text modifierDescription;
    @FXML
    ToolBar buttonToolbar;
    @FXML
    Pane toolbarPane;

    SuperPower superPower;
    DialogType dialogType;

    List<SuperPowerModifier> genericModifiers = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    private SuperPowerModifier chosenModifier;

    @FXML
    public void initialize() {

        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        modifierDescription.baselineOffsetProperty().add(10.0);

    }

    @FXML
    public void viewModifier() {
        SuperPowerModifier modifier = modifiersTableView.getSelectionModel().getSelectedItem();
        if (modifier != null) {
            modifierName.setText(modifier.getName() + " (" + modifier.getCostAsString() + ")");
            modifierDescription.setText(modifier.getDescription());
            setToolbarButtons(modifier);
        }
    }

    private void setToolbarButtons(SuperPowerModifier modifier) {
        buttonToolbar.getItems().clear();
        buttonToolbar.getItems().add(0, toolbarPane);

        if (dialogType == DialogType.CHOOSE) {
            setChooseToolbarButtons(modifier);
        } else if (dialogType == DialogType.REMOVE) {
            setRemoveToolbarButton(modifier);
        }
    }

    private void setChooseToolbarButtons(SuperPowerModifier modifier) {
        if (modifier.isLevelled()) {
            Button button = new Button("Select Modifier: " + modifier.getBaseCost() + " point(s)");
            button.setUserData(modifier.getBaseCost());
            button.setOnAction(actionEvent -> selectModifier(modifier, (Integer) button.getUserData()));
            buttonToolbar.getItems().add(1, button);

            Label label = new Label("Level(s):");
            buttonToolbar.getItems().add(1, label);
            Spinner<Integer> levelSpinner = new Spinner<>(1, (characterCreatorSingleton.getCurrentPowerPoints()/ modifier.getBaseCost()), 1);
            levelSpinner.setPrefWidth(50.0);
            levelSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                button.setUserData(newValue * modifier.getBaseCost());
                button.setText("Select Modifier: " + newValue * modifier.getBaseCost() + " point(s)");
            });
            buttonToolbar.getItems().add(2, levelSpinner);

        } else if (modifier.getSteppedCosts()!= null && modifier.getSteppedCosts().length > 1) {
            int counter = 1;
            for (int steppedCost : modifier.getSteppedCosts()) {
                Button button = new Button("Select Modifier: " + steppedCost + " point(s)");
                button.setOnAction(actionEvent -> selectModifier(modifier, steppedCost));
                buttonToolbar.getItems().add(counter, button);
                counter++;
            }
        } else {
            Button button = new Button("Select Modifier: " + modifier.getBaseCost() + " point(s)");
            button.setOnAction(actionEvent -> selectModifier(modifier, modifier.getBaseCost()));
            buttonToolbar.getItems().add(1, button);

            if (modifier.getBaseCost() > (characterCreatorSingleton.getCurrentPowerPoints())) {
              button.setDisable(true);
            }
        }
    }

    private void setRemoveToolbarButton(SuperPowerModifier modifier) {
        Button button = new Button ("Remove Modifier");
        button.setOnAction(actionEvent -> selectModifier(modifier, modifier.getPointsSpentOn()));
        buttonToolbar.getItems().add(1, button);
    }

    private void selectModifier(SuperPowerModifier modifier, int cost) {
        modifier.setPointsSpentOn(cost);
        chosenModifier = modifier;

        Stage stage = (Stage) buttonToolbar.getScene().getWindow();
        stage.close();
    }

    public void setUp(SuperPower superPower, DialogType dialogType) {
        this.superPower = superPower;
        this.dialogType = dialogType;
        setUpModifiersTableView();
        if (dialogType == DialogType.CHOOSE) {
            modifiersTableView.getItems().addAll(superPower.getAllModifiers());
            loadGenericModifiers();
        } else if (dialogType == DialogType.REMOVE) {
            modifiersTableView.getItems().addAll(superPower.getChosenModifiers());
        }
    }

    private void setUpModifiersTableView() {
        TableColumn<SuperPowerModifier, String> modifierColumn = new TableColumn<>("Modifiers");
        TableColumn<SuperPowerModifier, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SuperPowerModifier, String> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(superPowerModifier -> new SimpleStringProperty(superPowerModifier.getValue().getCostAsString()));

        modifierColumn.getColumns().add(nameColumn);
        modifierColumn.getColumns().add(costColumn);
        modifiersTableView.getColumns().add(modifierColumn);
    }

    private void loadGenericModifiers() {
        try {
            List<SuperPowerModifier> loadedModifiers = objectMapper.readValue(App.class.getResource("datafiles/spc/genericModifiers.json"), new TypeReference<>() {});
            genericModifiers.addAll(loadedModifiers);
            modifiersTableView.getItems().addAll(loadedModifiers);
        } catch (IOException e) {
            System.out.println("Error when trying to load genericModifiers.json: " + e.getMessage());
        }
    }

    public SuperPowerModifier getChosenModifier() {
        return chosenModifier;
    }

    public enum DialogType {
        CHOOSE, REMOVE;
    }
}
