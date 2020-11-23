package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.lairdham.App;
import org.lairdham.Utils;
import org.lairdham.models.Character;
import org.lairdham.models.CharacterCreatorSingleton;
import org.lairdham.models.Hindrance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class HindrancesController {

    @FXML
    ImageView topTitleImage;
    @FXML
    Label hindrancePointsLabel;
    @FXML
    Label majorHindrancesLabel;
    @FXML
    Label minorHindrancesLabel;

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

    CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();

        try {
            topTitleImage.setImage(new Image(new FileInputStream("src/main/resources/org/lairdham/images/question-mark.png"), 15, 15, true, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        updateCounterLabels();

        List<Hindrance> hindrancesList;
        try {
            hindrancesList = new ObjectMapper().readValue(App.class.getResource("datafiles/hindrancesCore.json"), new TypeReference<>() {});
            hindrancesList.forEach(hindrance ->{
                hindrancesTableView.getItems().add(hindrance);
            });
        } catch (IOException ex) {
            System.out.println("Error when trying to load hindrancesCore.json");
        }

        if (characterInProgress.getAllHindrances() != null &&
                !characterInProgress.getAllHindrances().isEmpty()) {
            selectedHindrancesTableView.getItems().addAll(characterInProgress.getAllHindrances());
        }

        resizeSelectedHindrancesTable();
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
    private void mouseOver() {
        App.setCursor(Cursor.HAND);
    }

    @FXML
    private void mouseOff() {
        App.setCursor(Cursor.DEFAULT);
    }

    @FXML
    private void showHindrancesInfo() throws IOException {
        Utils.showPopup("Hindrances and Hindrance Points",
                "Hindrances are character flaws and physical handicaps that occasionally make life a little tougher for your hero. " +
                        "Depending on the setting you can take a certain number of Major and Minor Hindrances. A Major Hindrance is worth 2 Hindrance Points, while a Minor Hindrance is worth 1.\n\n" +
                        "For 2 Hindrance Points you can:\n\t- Raise an attribute one die type, or\n\t- Choose an Edge\n For 1 Hindrance Point you can:\n\t- Gain another Skill Point, or\n\t- Gain additional money equal to your starting funds.",
                TextAlignment.LEFT);
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
        ObservableList<Hindrance> selectedHindrances = selectedHindrancesTableView.getItems();
        if (!selectedHindrances.contains(selectedHindrance)
                && characterCreatorSingleton.getRemainingHindrances(selectedHindrance.getType()) > 0) {

            selectedHindrances.add(selectedHindrance);
            characterCreatorSingleton.adjustHindrancesChosen(selectedHindrance.getType(), 1);
            updateCounterLabels();
            if (characterCreatorSingleton.allHindrancesChosen()) {
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
            characterCreatorSingleton.adjustHindrancesChosen(selectedHindrance.getType(), -1);
            updateCounterLabels();
        }
        removeHindranceButton.setDisable(true);

    }

    private void updateCounterLabels() {
        hindrancePointsLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
        majorHindrancesLabel.setText("Major Hindrances Remaining: " + characterCreatorSingleton.getRemainingHindrances(Hindrance.HindranceType.Major));
        minorHindrancesLabel.setText("Minor Hindrances Remaining: " + characterCreatorSingleton.getRemainingHindrances(Hindrance.HindranceType.Minor));
    }

    private void resizeSelectedHindrancesTable() {
        ObservableList<TableColumn<Hindrance, ?>> columns = selectedHindrancesTableView.getColumns();
        DoubleBinding usedWidth = columns.get(0).widthProperty().add(columns.get(1).widthProperty());
        columns.get(2).prefWidthProperty().bind(selectedHindrancesTableView.widthProperty().subtract(usedWidth));
    }
}
