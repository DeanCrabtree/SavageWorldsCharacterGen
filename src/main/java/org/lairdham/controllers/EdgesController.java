package org.lairdham.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.lairdham.App;
import org.lairdham.Utils;
import org.lairdham.models.*;
import org.lairdham.models.Character;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EdgesController {


    @FXML
    public ImageView topTitleImage;
    @FXML
    public TableView<Edge> edgesTableView;
    @FXML
    public TableView<Edge> selectedEdgesTableView;
    @FXML
    public Label edgeDescriptionTitle;
    @FXML
    public Text edgeRequirements;
    @FXML
    public Text edgeLongDescription;
    @FXML
    public Text edgeShortDescription;
    @FXML
    public Button selectEdgeButton;

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() throws JsonProcessingException {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();

        try {
            topTitleImage.setImage(new Image(new FileInputStream("src/main/resources/org/lairdham/images/question-mark.png"), 15, 15, true, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Edge> edgesList;
        try {
            edgesList = new ObjectMapper().readValue(App.class.getResource("datafiles/edgesCore.json"), new TypeReference<>() {});
            edgesList.forEach(edge ->{
                edgesTableView.getItems().add(edge);
            });
        } catch (IOException ex) {
            System.out.println("Error when trying to load edgesCore.json: " + ex);
        }

        resizeSelectedEdgesTable();
    }

    @FXML
    public void nextPage() {

    }

    @FXML
    public void prevPage() throws IOException {
        App.setRoot("traits");
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
    private void showEdgesInfo() throws IOException {
        Utils.showPopup("Hindrances and Hindrance Points",
                "Hindrances are character flaws and physical handicaps that occasionally make life a little tougher for your hero. " +
                        "Depending on the setting you can take a certain number of Major and Minor Hindrances. A Major Hindrance is worth 2 Hindrance Points, while a Minor Hindrance is worth 1.\n\n" +
                        "For 2 Hindrance Points you can:\n\t- Raise an attribute one die type, or\n\t- Choose an Edge\n For 1 Hindrance Point you can:\n\t- Gain another Skill Point, or\n\t- Gain additional money equal to your starting funds.",
                TextAlignment.LEFT);
    }

    @FXML
    public void viewEdge() {
        Edge selectedEdge = edgesTableView.getSelectionModel().getSelectedItem();
        if (selectedEdge != null) {
            edgeDescriptionTitle.setText(selectedEdge.getName() + " (" + selectedEdge.getType() + ")");
            edgeLongDescription.setText(selectedEdge.getLongDescription());
            edgeShortDescription.setText("TL;DR " + selectedEdge.getShortDescription());
            String requirementString = selectedEdge.getRequirements().toString();
            edgeRequirements.setText("Requirements: " + requirementString.substring(1, requirementString.length()-1));

            if (requirementsMet(selectedEdge)) {
                selectEdgeButton.setDisable(false);
            } else {
                selectEdgeButton.setDisable(true);
            }
        }

    }

    private boolean requirementsMet(Edge selectedEdge) {

        for (Requirement requirement : selectedEdge.getRequirements()) {
            switch (requirement.getType()) {

                case Attribute:

                    break;

                case Edge:
                    break;

                case Rank:
                    break;

                case Skill:
                    break;
            }
        }
        return false;
    }

    @FXML
    public void selectEdge() {

    }

    @FXML
    public void viewSelectedEdge(MouseEvent mouseEvent) {

    }

    @FXML
    public void removeSelectedEdge(ActionEvent actionEvent) {

    }

    private void resizeSelectedEdgesTable() {
        ObservableList<TableColumn<Edge, ?>> columns = selectedEdgesTableView.getColumns();
        DoubleBinding usedWidth = columns.get(0).widthProperty().add(columns.get(1).widthProperty());
        columns.get(2).prefWidthProperty().bind(selectedEdgesTableView.widthProperty().subtract(usedWidth));
    }
}
