package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.lairdham.App;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.Hindrance;

import java.io.IOException;
import java.util.List;

public class HindrancesController {

    @FXML
    TableView<Hindrance> hindrancesTableView;
    @FXML
    TableView<Hindrance> selectedHindrancesTableView;

    @FXML
    Label hindranceDescriptionTitle;
    @FXML
    Text hindranceLongDescription;
    @FXML
    Text hindranceShortDescription;
    @FXML
    Button selectHindranceButton;
    @FXML
    Button removeHindranceButton;
    @FXML
    Button nextButton;

    CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();

        List<Hindrance> hindrancesList = null;
        try {
            hindrancesList = new ObjectMapper().readValue(App.class.getResource("datafiles/hindrancesCore.json"), new TypeReference<>() {});
        } catch (IOException ex) {
            System.out.println("Error when trying to load hindrancesCore.json");
        }

        if (hindrancesList != null) {
            hindrancesList.forEach(hindrance ->{
                hindrancesTableView.getItems().add(hindrance);
            });
        }

        if (characterInProgress.getAllHindrances() != null &&
                !characterInProgress.getAllHindrances().isEmpty()) {
            selectedHindrancesTableView.getItems().addAll(characterInProgress.getAllHindrances());
        }

        if (characterCreatorSingleton.allHindrancesChosen()){
            nextButton.setDisable(false);
        }

        resizeSelectedHindrancesTable();
    }

    private void resizeSelectedHindrancesTable() {
        ObservableList<TableColumn<Hindrance, ?>> columns = selectedHindrancesTableView.getColumns();
        DoubleBinding usedWidth = columns.get(0).widthProperty().add(columns.get(1).widthProperty());
        columns.get(2).prefWidthProperty().bind(selectedHindrancesTableView.widthProperty().subtract(usedWidth));
    }

    @FXML
    private void nextPage() throws IOException {
        App.setRoot("traits");
        characterInProgress.setHindrances(selectedHindrancesTableView.getItems());
        characterCreatorSingleton.setCharacter(characterInProgress);
    }

    @FXML
    private void prevPage() throws IOException {
        App.setRoot("ancestrySelect");
    }

    @FXML
    public void viewHindrance() {
        Hindrance selectedHindrance = hindrancesTableView.getSelectionModel().getSelectedItem();
        if (selectedHindrance != null) {
            hindranceDescriptionTitle.setText(selectedHindrance.getName() + " (" + selectedHindrance.getType().name() + ")");
            hindranceLongDescription.setText(selectedHindrance.getLongDescription());
            hindranceShortDescription.setText("TL;DR " + selectedHindrance.getShortDescription());
            if (!characterCreatorSingleton.allHindrancesChosen()) {
                selectHindranceButton.setDisable(false);
            }
        }
    }

    @FXML
    public void selectHindrance() {
        Hindrance selectedHindrance = hindrancesTableView.getSelectionModel().getSelectedItem();
        ObservableList<Hindrance> alreadySelectedHindrances = selectedHindrancesTableView.getItems();
        if (!alreadySelectedHindrances.contains(selectedHindrance)
                && characterCreatorSingleton.getHindrancesAllowed(selectedHindrance.getType()) > 0) {

            alreadySelectedHindrances.add(selectedHindrance);
            characterCreatorSingleton.adjustHindrancesAllowed(selectedHindrance.getType(), -1);
            if (characterCreatorSingleton.allHindrancesChosen()) {
                nextButton.setDisable(false);
                selectHindranceButton.setDisable(true);
            }
        }
        resizeSelectedHindrancesTable();
    }

    @FXML
    public void viewSelectedHindrance() {
        Hindrance selectedHindrance = selectedHindrancesTableView.getSelectionModel().getSelectedItem();
        if (selectedHindrance != null) {
            removeHindranceButton.setDisable(false);
        }
    }

    @FXML
    public void removeSelectedHindrance() {
        Hindrance selectedHindrance = selectedHindrancesTableView.getSelectionModel().getSelectedItem();
        if (selectedHindrance != null) {
            selectedHindrancesTableView.getItems().remove(selectedHindrance);
            characterCreatorSingleton.adjustHindrancesAllowed(selectedHindrance.getType(), 1);
            if (!characterCreatorSingleton.allHindrancesChosen()) {
                nextButton.setDisable(true);
            }
        }
        removeHindranceButton.setDisable(true);

    }
}
