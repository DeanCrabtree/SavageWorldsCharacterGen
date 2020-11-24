package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.lairdham.App;
import org.lairdham.Utils;
import org.lairdham.models.Character;
import org.lairdham.models.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EdgesController {



    @FXML
    public Label edgesAvailableLabel;
    @FXML
    public Label hindrancePointsLabel;
    @FXML
    public TableView<Edge> edgesTableView;
    @FXML
    public TableView<Edge> selectedEdgesTableView;
    @FXML
    public Label edgeDescriptionTitle;
    @FXML
    public TextFlow edgeRequirements;
    @FXML
    public Text edgeLongDescription;
    @FXML
    public Text edgeShortDescription;
    @FXML
    public Button selectEdgeButton;
    @FXML
    public Label requirementsNotMetLabel;
    @FXML
    public Button removeEdgeButton;

    private HashMap<String, Edge> allEdges = new HashMap<>();
    private List<Edge> lockedEdges = new ArrayList<>();

    private CharacterCreatorSingleton characterCreatorSingleton;
    private Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        List<Edge> edgesList;
        try {
            edgesList = new ObjectMapper().readValue(App.class.getResource("datafiles/edgesCore.json"), new TypeReference<>() {});
            edgesList.forEach(edge ->{
                edgesTableView.getItems().add(edge);
                allEdges.put(edge.getName(), edge);
            });
        } catch (IOException ex) {
            System.out.println("Error when trying to load edgesCore.json: " + ex);
        }

        characterCreatorSingleton.getSettingRequiredEdges().forEach(requiredEdge -> {
            if (!characterInProgress.getAllEdges().contains(requiredEdge)) {
                characterInProgress.addEdge(requiredEdge);
            }
        });

        List<Edge> preSelectedEdges = characterInProgress.getAllEdges();
        if (preSelectedEdges != null) {
            selectedEdgesTableView.getItems().addAll(preSelectedEdges);
            preSelectedEdges.forEach(edge -> {
                if (!requirementsMet(edge)) {
                    selectedEdgesTableView.getItems().remove(edge);
                    characterCreatorSingleton.adjustEdgePoints(1);
                }
            });
            preSelectedEdges.forEach(this::lockRequiredEdges);
            edgeRequirements.getChildren().clear();
            selectedEdgesTableView.getSelectionModel().select(null);
        }

        updateCounterLabels();
        resizeSelectedEdgesTable();
    }

    private void updateCounterLabels() {
        edgesAvailableLabel.setText("Edges: " + characterCreatorSingleton.getEdgePoints());
        hindrancePointsLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
    }

    @FXML
    public void nextPage() {

    }

    @FXML
    public void prevPage() throws IOException {
        App.setRoot("traits");
        characterInProgress.setEdges(selectedEdgesTableView.getItems());
        characterCreatorSingleton.setCharacter(characterInProgress);
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
            edgeRequirements.getChildren().clear();
            edgeDescriptionTitle.setText(selectedEdge.getName() + " (" + selectedEdge.getType() + ")");
            edgeLongDescription.setText(selectedEdge.getLongDescription());
            edgeShortDescription.setText("TL;DR " + selectedEdge.getShortDescription());

            if (requirementsMet(selectedEdge)) {
                selectEdgeButton.setDisable(false);
                requirementsNotMetLabel.setVisible(false);
            } else {
                selectEdgeButton.setDisable(true);
                requirementsNotMetLabel.setVisible(true);
            }
        }

    }

    @FXML
    public void selectEdge() {
        Edge selectedEdge = edgesTableView.getSelectionModel().getSelectedItem();
        ObservableList<Edge> selectedEdges = selectedEdgesTableView.getItems();
        if (!selectedEdges.contains(selectedEdge)
                && characterCreatorSingleton.getEdgePoints() > 0) {
            selectedEdges.add(selectedEdge);
            lockRequiredEdges(selectedEdge);
            characterCreatorSingleton.adjustEdgePoints(-1);
            updateCounterLabels();
            resizeSelectedEdgesTable();
        }
    }

    @FXML
    public void viewSelectedEdge() {
        Edge selectedEdge = selectedEdgesTableView.getSelectionModel().getSelectedItem();
        if (selectedEdge != null) {
            if (!lockedEdges.contains(selectedEdge) && !characterCreatorSingleton.getSettingRequiredEdges().contains(selectedEdge)) {
                removeEdgeButton.setDisable(false);
            }else {
                removeEdgeButton.setDisable(true);
            }
        }
    }

    @FXML
    public void removeSelectedEdge() {
        Edge selectedEdge = selectedEdgesTableView.getSelectionModel().getSelectedItem();
        if (selectedEdge != null) {
            selectedEdgesTableView.getItems().remove(selectedEdge);
            unlockRequiredEdges(selectedEdge);
            characterCreatorSingleton.adjustEdgePoints(1);
            updateCounterLabels();
            removeEdgeButton.setDisable(true);
        }
        viewEdge();
    }

    private boolean requirementsMet(Edge selectedEdge) {
        boolean overallResult = true;
        List<Label> requirementTexts = new ArrayList<>();
        requirementTexts.add(new Label("Requirements: "));
        for (Requirement requirement : selectedEdge.getRequirements()) {
            boolean specificRequirementResult = false;
            switch (requirement.getType()) {

                case Attribute:
                    Attribute requiredAttribute = (Attribute) requirement.getRequiredValue();
                    Attribute characterAttribute = characterInProgress.getAllAttributes().get(requiredAttribute.getName());
                    if (characterAttribute.getValue().isGreaterThanOrEqualTo(requiredAttribute.getValue())) {
                        specificRequirementResult = true;
                    }
                    break;

                case Edge:
                    Edge requiredEdge = allEdges.get((String)requirement.getRequiredValue());
                    if (selectedEdgesTableView.getItems().contains(requiredEdge)) {
                        specificRequirementResult = true;
                    }
                    break;

                case Rank:
                    Rank requiredRank = (Rank) requirement.getRequiredValue();
                    Rank characterRank = characterInProgress.getRank();
                    if (characterRank.isGreaterThanOrEqualTo(requiredRank)) {
                        specificRequirementResult = true;
                    }
                    break;

                case Skill:
                    Skill requiredSkill = (Skill) requirement.getRequiredValue();
                    Skill characterSkill = characterInProgress.getSkill(requiredSkill.getName());
                    if (characterSkill != null && characterSkill.getValue().isGreaterThanOrEqualTo(requiredSkill.getValue())) {
                        specificRequirementResult = true;
                    }
                    break;

                default:
                    System.out.println("Unrecognised requirement type");
            }

            Label requirementText = new Label(requirement.toString());
            if (!specificRequirementResult) {
                requirementText.setTextFill(Color.CRIMSON);
                overallResult = false;
            }
            requirementTexts.add(requirementText);
            requirementTexts.add(new Label(", "));
        }
        requirementTexts.remove(requirementTexts.size()-1);
        edgeRequirements.getChildren().addAll(requirementTexts);
        return overallResult;
    }

    private void lockRequiredEdges(Edge selectedEdge) {
        List<Requirement> requiredEdges = selectedEdge.getRequirements().stream().filter(requirement -> requirement.getType().equals(Requirement.RequirementType.Edge)).collect(Collectors.toList());
        requiredEdges.forEach(requirement -> {
            selectedEdgesTableView.getSelectionModel().select(allEdges.get((String) requirement.getRequiredValue()));
            lockedEdges.add(selectedEdgesTableView.getSelectionModel().getSelectedItem());
        });
        selectedEdgesTableView.getSelectionModel().select(selectedEdge);
    }

    private void unlockRequiredEdges(Edge selectedEdge) {
        List<Requirement> requiredEdges = selectedEdge.getRequirements().stream().filter(requirement -> requirement.getType().equals(Requirement.RequirementType.Edge)).collect(Collectors.toList());
        requiredEdges.forEach(requirement -> {
            selectedEdgesTableView.getSelectionModel().select(allEdges.get((String) requirement.getRequiredValue()));
            lockedEdges.remove(selectedEdgesTableView.getSelectionModel().getSelectedItem());
        });
    }

    private void resizeSelectedEdgesTable() {
        ObservableList<TableColumn<Edge, ?>> columns = selectedEdgesTableView.getColumns();
        setLengthOfMostRecentEntry(columns.get(0));
        DoubleBinding usedWidth = columns.get(0).widthProperty().add(columns.get(1).widthProperty());
        columns.get(2).prefWidthProperty().bind(selectedEdgesTableView.widthProperty().subtract(usedWidth));
    }

    private void setLengthOfMostRecentEntry(TableColumn column) {
        Object cellData = column.getCellData(selectedEdgesTableView.getItems().size()-1);
        if(cellData != null) {
            Text data = new Text(cellData.toString());
            column.setMinWidth(data.getLayoutBounds().getWidth() + 10.0);
        }
    }
}
