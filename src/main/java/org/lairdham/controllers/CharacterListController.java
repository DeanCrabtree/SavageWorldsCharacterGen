package org.lairdham.controllers;

import javafx.fxml.FXML;
import org.lairdham.App;
import org.lairdham.models.*;
import org.lairdham.models.Character;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterListController {

    @FXML
    private void createNewCharacter() throws IOException {
        CharacterCreatorSingleton.getInstance().setCharacter(new Character());
        //TODO: This should go in settings choice page, but here for now for development
        List<Edge> settingRequiredEdges = new ArrayList<>();
        Edge edge = new Edge();
        edge.setName("Arcane Background (Super Powers)");
        edge.setType(Edge.EdgeType.Background);
        edge.setShortDescription("Free Edge. Grants Power Points equal to the Power Level of the Campaign");
        edge.setLongDescription("This Edge replaces the version from Savage Worlds in a supers campaign and is a free Edge (on top of the usual free Edge for being human). It grants a number of Power Points equal to the Power Level of the campaign.\n No arcane skill is required. Super powers work \"at will\" and require no roll unless the specific power says otherwise.");
        List<Requirement> requirements = new ArrayList<>();
        Requirement<Rank> requirement = new Requirement<>();
        requirement.setType(Requirement.RequirementType.Rank);
        requirement.setRequiredValue(Rank.Novice);
        requirements.add(requirement);
        edge.setRequirements(requirements);
        settingRequiredEdges.add(edge);
        CharacterCreatorSingleton.getInstance().setSettingRequiredEdges(settingRequiredEdges);
        App.setRoot("settingSelect");
    }
}
