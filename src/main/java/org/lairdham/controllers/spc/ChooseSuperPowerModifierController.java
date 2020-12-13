package org.lairdham.controllers.spc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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

    List<SuperPowerModifier> genericModifiers = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

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

        if (modifier.isLevelled()) {


        } else if (modifier.getSteppedCosts()!= null && modifier.getSteppedCosts().length > 1) {
            int counter = 1;
            for (int steppedCost : modifier.getSteppedCosts()) {
                Button button = new Button("Select Modifier: " + steppedCost);
                button.setOnAction(actionEvent -> buyModifier(steppedCost));
                buttonToolbar.getItems().add(counter, button);
                counter++;
            }
        } else {
            Button button = new Button("Select Modifier: " + modifier.getCost());
            button.setOnAction(actionEvent -> buyModifier(modifier.getCost()));
            buttonToolbar.getItems().add(1, button);
        }



    }

    private void buyModifier(int cost) {
        System.out.println("Buy Cost: " + cost);

    }

    public void setSuperPower(SuperPower superPower) {
        this.superPower = superPower;
        modifiersTableView.getItems().addAll(superPower.getAllModifiers());
        loadGenericModifiers();
    }

    private void loadGenericModifiers() {
        TableColumn<SuperPowerModifier, String> modifierColumn = new TableColumn<>("Modifiers");
        TableColumn<SuperPowerModifier, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SuperPowerModifier, String> costColumn = new TableColumn<>("Cost");
        costColumn.setCellValueFactory(superPowerModifier -> new SimpleStringProperty(superPowerModifier.getValue().getCostAsString()));

        modifierColumn.getColumns().add(nameColumn);
        modifierColumn.getColumns().add(costColumn);
        modifiersTableView.getColumns().add(modifierColumn);

        try {
            List<SuperPowerModifier> loadedModifiers = objectMapper.readValue(App.class.getResource("datafiles/spc/genericModifiers.json"), new TypeReference<>() {});
            genericModifiers.addAll(loadedModifiers);
            modifiersTableView.getItems().addAll(loadedModifiers);
        } catch (IOException e) {
            System.out.println("Error when trying to load genericModifiers.json");
        }
    }

    @FXML
    public void buyModifier() {
    }
}
