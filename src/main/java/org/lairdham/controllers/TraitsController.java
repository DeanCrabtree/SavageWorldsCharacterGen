package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import org.lairdham.App;
import org.lairdham.Utils;
import org.lairdham.models.Character;
import org.lairdham.models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TraitsController {

    @FXML
    public Button buyAttributeButton;
    @FXML
    public Button sellAttributeButton;
    @FXML
    public Button buySkillButton;
    @FXML
    public Button sellSkillButton;
    @FXML
    Label attributePointLabel;
    @FXML
    Label skillPointLabel;
    @FXML
    Label hindrancePointLabel;

    //Attribute Images
    @FXML
    ImageView agilityImageView;
    @FXML
    ImageView smartsImageView;
    @FXML
    ImageView spiritImageView;
    @FXML
    ImageView strengthImageView;
    @FXML
    ImageView vigorImageView;

    //Skill Images
    @FXML
    public ImageView boatingImageView;
    @FXML
    public ImageView drivingImageView;
    @FXML
    public ImageView fightingImageView;
    @FXML
    public ImageView lockpickingImageView;
    @FXML
    public ImageView pilotingImageView;
    @FXML
    public ImageView ridingImageView;
    @FXML
    public ImageView shootingImageView;
    @FXML
    public ImageView stealthImageView;
    @FXML
    public ImageView swimmingImageView;
    @FXML
    public ImageView throwingImageView;
    @FXML
    public ImageView gamblingImageView;
    @FXML
    public ImageView healingImageView;
    @FXML
    public ImageView investigationImageView;
    @FXML
    public ImageView noticeImageView;
    @FXML
    public ImageView repairImageView;
    @FXML
    public ImageView streetwiseImageView;
    @FXML
    public ImageView survivalImageView;
    @FXML
    public ImageView tauntImageView;
    @FXML
    public ImageView trackingImageView;
    @FXML
    public ImageView intimidationImageView;
    @FXML
    public ImageView persuasionImageView;
    @FXML
    public ImageView climbingImageView;

    @FXML
    HBox skillBox1;
    @FXML
    public HBox skillBox2;

    @FXML
    public Button nextButton;

    Image noValueImage;
    Image d4Image;
    Image d6Image;
    Image d8Image;
    Image d10Image;
    Image d12Image;

    //Map of trait values to image representing that value
    HashMap<TraitValue, Image> imageMap = new HashMap<>();

    //Map of skills to imageviews representing them
    HashMap<Skill, ImageView> skillImageViewMap = new HashMap<>();

    HashMap<String, List<String>> traitDescriptions = new HashMap<>();

    CharacterCreatorSingleton characterCreatorSingleton;
    Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());
        skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());
        hindrancePointLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
        loadTraitImages();
        loadTraitDescriptions();
        defaultImageViews();
        updateButtonsAvailability();
    }

    @FXML
    private void nextPage() throws IOException {
        CharacterCreatorSingleton.getInstance().setCharacter(characterInProgress);
        App.setRoot("edges");
    }

    @FXML
    private void prevPage() throws IOException {
        CharacterCreatorSingleton.getInstance().setCharacter(characterInProgress);
        App.setRoot("hindrances");
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
    private void showTraitInfo(MouseEvent event) throws IOException {
        Label clickedLabel = (Label) event.getSource();
        Utils.showPopup(clickedLabel.getText() + " " + traitDescriptions.get(clickedLabel.getText()).get(0),
                traitDescriptions.get(clickedLabel.getText()).get(1));
    }

    @FXML
    private void showHindrancesInfo() throws IOException {
        Utils.showPopup("Hindrance Points",
                        "For 2 Hindrance Points you can:\n\t- Raise an attribute one die type, or\n\t- Choose an Edge\n For 1 Hindrance Point you can:\n\t- Gain another Skill Point, or\n\t- Gain additional money equal to your starting funds.",
                TextAlignment.LEFT);
    }

    private void increaseAttribute(Attribute attribute, ImageView imageView) {
        if (characterCreatorSingleton.getAttributePoints() > 0 &&
                !attribute.getValue().equals(TraitValue.d12)) {
            adjustAttributePoints(-1);

            characterInProgress.adjustTrait(attribute, 1);
            recoverSpentSkillPointsWhenAttributeIncreased(attribute);
            imageView.setImage(imageMap.get(attribute.getValue()));

            if (characterCreatorSingleton.getAttributePoints() == 0) {
                skillBox1.setDisable(false);
                skillBox2.setDisable(false);
                adjustSkillImageContrasts(1.0, 0.4);

            }

            updateButtonsAvailability();

        }
    }

    private void reduceAttribute(Attribute attribute, ImageView imageView) {
        if (!attribute.getValue().equals(TraitValue.d4)) {
            adjustAttributePoints(1);

            characterInProgress.adjustTrait(attribute, -1);
            imageView.setImage(imageMap.get(attribute.getValue()));
            reduceSkillLevelWithAttribute(attribute);
            updateButtonsAvailability();
        }
    }

    public void increaseAgilityValue() {
        increaseAttribute(characterInProgress.getAgility(), agilityImageView);
    }
    public void reduceAgilityValue() {
        reduceAttribute(characterInProgress.getAgility(), agilityImageView);
    }

    public void increaseSmartsValue() {
        increaseAttribute(characterInProgress.getSmarts(), smartsImageView);
    }
    public void reduceSmartsValue() {
        reduceAttribute(characterInProgress.getSmarts(), smartsImageView);
    }

    public void increaseSpiritValue() {
        increaseAttribute(characterInProgress.getSpirit(), spiritImageView);
    }
    public void reduceSpiritValue() {
        reduceAttribute(characterInProgress.getSpirit(), spiritImageView);
    }

    public void increaseStrengthValue() {
        increaseAttribute(characterInProgress.getStrength(), strengthImageView);
    }
    public void reduceStrengthValue() {
        reduceAttribute(characterInProgress.getStrength(), strengthImageView);
    }

    public void increaseVigorValue() {
        increaseAttribute(characterInProgress.getVigor(), vigorImageView);
    }
    public void reduceVigorValue() {
        reduceAttribute(characterInProgress.getVigor(), vigorImageView);
    }

    private void increaseSkill(Skill skill) {
        if (characterCreatorSingleton.getSkillPoints() > 0
                && !skill.getValue().equals(TraitValue.d12)) {
            if (skill.isEqualToOrGreaterThanLinkedAttribute()) {
                if (characterCreatorSingleton.getSkillPoints() > 1) {
                    adjustSkillPoints(-2);
                } else {
                    return;
                }
            } else {
                adjustSkillPoints(-1);
            }

            characterInProgress.adjustTrait(skill, 1);
            skillImageViewMap.get(skill).setImage(imageMap.get(skill.getValue()));

            updateButtonsAvailability();
        }

    }
    private void reduceSkill(Skill skill) {
        if (!skill.getValue().equals(TraitValue.noValue)) {
            if (skill.isGreaterThanLinkedAttribute()) {
                adjustSkillPoints(2);
            } else {
                adjustSkillPoints(1);
            }

            characterInProgress.adjustTrait(skill, -1);
            skillImageViewMap.get(skill).setImage(imageMap.get(skill.getValue()));

            updateButtonsAvailability();
        }
    }

    public void increaseBoatingValue() {
        increaseSkill(characterInProgress.getBoating());

    }
    public void reduceBoatingValue() {
        reduceSkill(characterInProgress.getBoating());
    }

    public void increaseDrivingValue() {
        increaseSkill(characterInProgress.getDriving());
    }
    public void reduceDrivingValue() {
        reduceSkill(characterInProgress.getDriving());
    }

    public void increaseFightingValue() {
        increaseSkill(characterInProgress.getFighting());
    }
    public void reduceFightingValue() {
        reduceSkill(characterInProgress.getFighting());
    }

    public void increaseLockpickingValue() {
        increaseSkill(characterInProgress.getLockpicking());
    }
    public void reduceLockpickingValue() {
        reduceSkill(characterInProgress.getLockpicking());
    }

    public void increasePilotingValue() {
        increaseSkill(characterInProgress.getPiloting());
    }
    public void reducePilotingValue() {
        reduceSkill(characterInProgress.getPiloting());
    }

    public void increaseRidingValue() {
        increaseSkill(characterInProgress.getRiding());
    }
    public void reduceRidingValue() {
        reduceSkill(characterInProgress.getRiding());
    }

    public void increaseShootingValue() {
        increaseSkill(characterInProgress.getShooting());
    }
    public void reduceShootingValue() {
        reduceSkill(characterInProgress.getShooting());
    }

    public void increaseStealthValue() {
        increaseSkill(characterInProgress.getStealth());
    }
    public void reduceStealthValue() {
        reduceSkill(characterInProgress.getStealth());
    }

    public void increaseSwimmingValue() {
        increaseSkill(characterInProgress.getSwimming());
    }
    public void reduceSwimmingValue() {
        reduceSkill(characterInProgress.getSwimming());
    }

    public void increaseThrowingValue() {
        increaseSkill(characterInProgress.getThrowing());
    }
    public void reduceThrowingValue() {
        reduceSkill(characterInProgress.getThrowing());
    }

    public void increaseGamblingValue() {
        increaseSkill(characterInProgress.getGambling());
    }
    public void reduceGamblingValue() {
        reduceSkill(characterInProgress.getGambling());
    }

    public void increaseHealingValue() {
        increaseSkill(characterInProgress.getHealing());
    }
    public void reduceHealingValue() {
        reduceSkill(characterInProgress.getHealing());
    }

    public void increaseInvestigationValue() {
        increaseSkill(characterInProgress.getInvestigation());
    }
    public void reduceInvestigationValue() {
        reduceSkill(characterInProgress.getInvestigation());
    }

    public void increaseNoticeValue() {
        increaseSkill(characterInProgress.getNotice());
    }
    public void reduceNoticeValue() {
        reduceSkill(characterInProgress.getNotice());
    }

    public void increaseRepairValue() {
        increaseSkill(characterInProgress.getRepair());
    }
    public void reduceRepairValue() {
        reduceSkill(characterInProgress.getRepair());
    }

    public void increaseStreetwiseValue() {
        increaseSkill(characterInProgress.getStreetwise());
    }
    public void reduceStreetwiseValue() {
        reduceSkill(characterInProgress.getStreetwise());
    }

    public void increaseSurvivalValue() {
        increaseSkill(characterInProgress.getSurvival());
    }
    public void reduceSurvivalValue() {
        reduceSkill(characterInProgress.getSurvival());
    }

    public void increaseTauntValue() {
        increaseSkill(characterInProgress.getTaunt());
    }
    public void reduceTauntValue() {
        reduceSkill(characterInProgress.getTaunt());
    }

    public void increaseTrackingValue() {
        increaseSkill(characterInProgress.getTracking());
    }
    public void reduceTrackingValue() {
        reduceSkill(characterInProgress.getTracking());
    }

    public void increaseIntimidationValue() {
        increaseSkill(characterInProgress.getIntimidation());
    }
    public void reduceIntimidationValue() {
        reduceSkill(characterInProgress.getIntimidation());
    }

    public void increasePersuasionValue() {
        increaseSkill(characterInProgress.getPersuasion());
    }
    public void reducePersuasionValue() {
        reduceSkill(characterInProgress.getPersuasion());
    }

    public void increaseClimbingValue() {
        increaseSkill(characterInProgress.getClimbing());
    }
    public void reduceClimbingValue() {
        reduceSkill(characterInProgress.getClimbing());
    }


    private void adjustAttributePoints(int value) {
        characterCreatorSingleton.adjustAttributePoints(value);
        attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());
    }

    private void adjustSkillPoints(int value) {
        characterCreatorSingleton.adjustSkillPoints(value);
        skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());
    }

    private void recoverSpentSkillPointsWhenAttributeIncreased(Attribute attribute) {
        List<Skill> relevantSkills = characterInProgress.getAllSkills().stream()
                .filter(skill -> skill.getLinkedAttribute().getName().equals(attribute.getName())).collect(Collectors.toList());
        relevantSkills.forEach(skill -> {
            if (skill.isEqualToOrGreaterThanLinkedAttribute()) {
                adjustSkillPoints(1);
            }
        });
    }

    private void reduceSkillLevelWithAttribute(Attribute attribute) {
        List<Skill> relevantSkills = characterInProgress.getAllSkills().stream()
                .filter(skill -> skill.getLinkedAttribute().getName().equals(attribute.getName())).collect(Collectors.toList());
        relevantSkills.forEach(skill -> {
            while (skill.isGreaterThanLinkedAttribute()) {
                if (skill.getValue().getNumericalValue() - attribute.getValue().getNumericalValue() > 2) {
                    adjustSkillPoints(2);
                } else {
                    adjustSkillPoints(1);
                }
                skill.decrementValue();

                skillImageViewMap.get(skill).setImage(imageMap.get(skill.getValue()));
            }
        });

        if (characterCreatorSingleton.getSkillPoints() > 0 && characterCreatorSingleton.getExtraSkillPointsBought() > 0) {
            sellSkillButton.setVisible(true);
        }

    }

    private void loadTraitImages() {
        try {
            noValueImage = new Image(new FileInputStream("src/main/resources/org/lairdham/images/noValueAttribute.PNG"));
            imageMap.put(TraitValue.noValue, noValueImage);
            d4Image = new Image(new FileInputStream("src/main/resources/org/lairdham/images/d4Attribute.PNG"));
            imageMap.put(TraitValue.d4, d4Image);
            d6Image = new Image(new FileInputStream("src/main/resources/org/lairdham/images/d6Attribute.PNG"));
            imageMap.put(TraitValue.d6, d6Image);
            d8Image = new Image(new FileInputStream("src/main/resources/org/lairdham/images/d8Attribute.PNG"));
            imageMap.put(TraitValue.d8, d8Image);
            d10Image = new Image(new FileInputStream("src/main/resources/org/lairdham/images/d10Attribute.PNG"));
            imageMap.put(TraitValue.d10, d10Image);
            d12Image = new Image(new FileInputStream("src/main/resources/org/lairdham/images/d12Attribute.PNG"));
            imageMap.put(TraitValue.d12, d12Image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadTraitDescriptions() {
        try {
            traitDescriptions = new ObjectMapper().readValue(App.class.getResource("datafiles/traitDescriptions.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            System.out.println("Error trying to load traitDescriptions.json");
        }
    }

    private void defaultImageViews() {

        agilityImageView.setImage(imageMap.get(characterInProgress.getAgility().getValue()));
        smartsImageView.setImage(imageMap.get(characterInProgress.getSmarts().getValue()));
        spiritImageView.setImage(imageMap.get(characterInProgress.getSpirit().getValue()));
        strengthImageView.setImage(imageMap.get(characterInProgress.getStrength().getValue()));
        vigorImageView.setImage(imageMap.get(characterInProgress.getVigor().getValue()));

        skillImageViewMap.put(characterInProgress.getBoating(), boatingImageView);
        skillImageViewMap.put(characterInProgress.getDriving(), drivingImageView);
        skillImageViewMap.put(characterInProgress.getFighting(), fightingImageView);
        skillImageViewMap.put(characterInProgress.getLockpicking(), lockpickingImageView);
        skillImageViewMap.put(characterInProgress.getPiloting(), pilotingImageView);
        skillImageViewMap.put(characterInProgress.getRiding(), ridingImageView);
        skillImageViewMap.put(characterInProgress.getShooting(), shootingImageView);
        skillImageViewMap.put(characterInProgress.getStealth(), stealthImageView);
        skillImageViewMap.put(characterInProgress.getSwimming(), swimmingImageView);
        skillImageViewMap.put(characterInProgress.getThrowing(), throwingImageView);
        skillImageViewMap.put(characterInProgress.getGambling(), gamblingImageView);
        skillImageViewMap.put(characterInProgress.getHealing(), healingImageView);
        skillImageViewMap.put(characterInProgress.getInvestigation(), investigationImageView);
        skillImageViewMap.put(characterInProgress.getNotice(), noticeImageView);
        skillImageViewMap.put(characterInProgress.getRepair(), repairImageView);
        skillImageViewMap.put(characterInProgress.getStreetwise(), streetwiseImageView);
        skillImageViewMap.put(characterInProgress.getSurvival(), survivalImageView);
        skillImageViewMap.put(characterInProgress.getTaunt(), tauntImageView);
        skillImageViewMap.put(characterInProgress.getTracking(), trackingImageView);
        skillImageViewMap.put(characterInProgress.getIntimidation(), intimidationImageView);
        skillImageViewMap.put(characterInProgress.getPersuasion(), persuasionImageView);
        skillImageViewMap.put(characterInProgress.getClimbing(), climbingImageView);

        for (Map.Entry<Skill, ImageView> entry : skillImageViewMap.entrySet()) {
            entry.getValue().setImage(imageMap.get(entry.getKey().getValue()));
        }

        if (characterCreatorSingleton.getAttributePoints() == 0 && characterCreatorSingleton.getHindrancePoints() > 1) {
            buyAttributeButton.setVisible(true);
        }

        if (characterCreatorSingleton.getSkillPoints() == 15) {
            adjustSkillImageContrasts(0.4, 0.8);
        } else {
            skillBox1.setDisable(false);
            skillBox2.setDisable(false);
        }

    }

    private void adjustSkillImageContrasts(double contrast, double brightness) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(contrast);
        colorAdjust.setBrightness(brightness);

        for (ImageView imageView : skillImageViewMap.values()) {
            imageView.setEffect(colorAdjust);
        }
    }

    public void spendHindrancePointsForAttributePoint() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Spend 2 Hindrance Points to gain 1 Attribute Point?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            characterCreatorSingleton.adjustHindrancePoints(-2);
            characterCreatorSingleton.adjustAttributePoints(1);
            characterCreatorSingleton.adjustExtraAttributePointsBought(1);
            attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());
            hindrancePointLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
        }

        updateButtonsAvailability();

    }

    public void spendAttributeForHindrancePoints() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Revert 1 Attribute Point into 2 Hindrance Points?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            characterCreatorSingleton.adjustHindrancePoints(2);
            characterCreatorSingleton.adjustAttributePoints(-1);
            characterCreatorSingleton.adjustExtraAttributePointsBought(-1);
            attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());
            hindrancePointLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
        }

        updateButtonsAvailability();
    }

    public void spendHindrancePointForSkillPoint() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Spend 1 Hindrance Point to gain 1 Skill Point?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            characterCreatorSingleton.adjustHindrancePoints(-1);
            characterCreatorSingleton.adjustSkillPoints(1);
            characterCreatorSingleton.adjustExtraSkillPointsBought(1);
            skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());
            hindrancePointLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
            sellSkillButton.setVisible(true);
        }

        updateButtonsAvailability();

    }

    public void spendSkillForHindrancePoint() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Revert 1 Skill Point into 1 Hindrance Point?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            characterCreatorSingleton.adjustHindrancePoints(1);
            characterCreatorSingleton.adjustSkillPoints(-1);
            characterCreatorSingleton.adjustExtraSkillPointsBought(-1);
            skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());
            hindrancePointLabel.setText("Hindrance Points: " + characterCreatorSingleton.getHindrancePoints());
        }

        updateButtonsAvailability();
    }

    private void updateButtonsAvailability() {
        buyAttributeButton.setVisible((characterCreatorSingleton.getHindrancePoints() > 1) && (characterCreatorSingleton.getAttributePoints()==0));
        sellAttributeButton.setVisible(characterCreatorSingleton.getExtraAttributePointsBought() > 0 && characterCreatorSingleton.getAttributePoints() > 0);

        buySkillButton.setVisible((characterCreatorSingleton.getHindrancePoints() > 0) && (characterCreatorSingleton.getSkillPoints()==0));
        sellSkillButton.setVisible(characterCreatorSingleton.getExtraSkillPointsBought() > 0 && characterCreatorSingleton.getSkillPoints() > 0);

        nextButton.setDisable(!(characterCreatorSingleton.getSkillPoints()==0 && characterCreatorSingleton.getAttributePoints()==0));
    }
}
