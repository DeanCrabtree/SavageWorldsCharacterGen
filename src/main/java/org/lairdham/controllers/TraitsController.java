package org.lairdham.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.lairdham.App;
import org.lairdham.Utils;
import org.lairdham.models.Character;
import org.lairdham.models.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class TraitsController {

    @FXML
    Label attributePointLabel;
    @FXML
    Label skillPointLabel;

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

    HashMap<TraitValue, Image> imageMap = new HashMap<>();
    HashMap<String, List<String>> traitDescriptions = new HashMap<>();

    CharacterCreatorSingleton characterCreatorSingleton;
    Character characterInProgress;

    @FXML
    protected void initialize() {
        characterCreatorSingleton = CharacterCreatorSingleton.getInstance();
        characterInProgress = characterCreatorSingleton.getCharacter();
        attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());
        skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());
        loadTraitImages();
        loadTraitDescriptions();
        defaultImageViews();

    }

    @FXML
    private void nextPage() throws IOException {
        CharacterCreatorSingleton.getInstance().setCharacter(characterInProgress);
        App.setRoot("edges");
    }

    @FXML
    private void prevPage() throws IOException {
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

    private void increaseAttribute(Attribute attribute, ImageView imageView) {
        if (characterCreatorSingleton.getAttributePoints() > 0 &&
                !attribute.getValue().equals(TraitValue.d12)) {
            adjustAttributePoints(-1);

            characterInProgress.adjustTrait(attribute, 1);
            imageView.setImage(imageMap.get(attribute.getValue()));
        }
    }
    private void reduceAttribute(Attribute attribute, ImageView imageView) {
        if (!attribute.getValue().equals(TraitValue.d4)) {
            adjustAttributePoints(1);

            characterInProgress.adjustTrait(attribute, -1);
            imageView.setImage(imageMap.get(attribute.getValue()));
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

    private void increaseSkill(Skill skill, ImageView imageView) {
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
            imageView.setImage(imageMap.get(skill.getValue()));
        }

    }
    private void reduceSkill(Skill skill, ImageView imageView) {
        if (!skill.getValue().equals(TraitValue.noValue)) {
            if (skill.isGreaterThanLinkedAttribute()) {
                adjustSkillPoints(2);
            } else {
                adjustSkillPoints(1);
            }

            characterInProgress.adjustTrait(skill, -1);
            imageView.setImage(imageMap.get(skill.getValue()));
        }
    }
    private void resetSkill(Skill skill, ImageView imageView) {
        skill.setValue(TraitValue.noValue);
        imageView.setImage(noValueImage);
    }

    public void increaseBoatingValue() {
        increaseSkill(characterInProgress.getBoating(), boatingImageView);

    }
    public void reduceBoatingValue() {
        reduceSkill(characterInProgress.getBoating(), boatingImageView);
    }

    public void increaseDrivingValue() {
        increaseSkill(characterInProgress.getDriving(), drivingImageView);
    }
    public void reduceDrivingValue() {
        reduceSkill(characterInProgress.getDriving(), drivingImageView);
    }

    public void increaseFightingValue() {
        increaseSkill(characterInProgress.getFighting(), fightingImageView);
    }
    public void reduceFightingValue() {
        reduceSkill(characterInProgress.getFighting(), fightingImageView);
    }

    public void increaseLockpickingValue() {
        increaseSkill(characterInProgress.getLockpicking(), lockpickingImageView);
    }
    public void reduceLockpickingValue() {
        reduceSkill(characterInProgress.getLockpicking(), lockpickingImageView);
    }

    public void increasePilotingValue() {
        increaseSkill(characterInProgress.getPiloting(), pilotingImageView);
    }
    public void reducePilotingValue() {
        reduceSkill(characterInProgress.getPiloting(), pilotingImageView);
    }

    public void increaseRidingValue() {
        increaseSkill(characterInProgress.getRiding(), ridingImageView);
    }
    public void reduceRidingValue() {
        reduceSkill(characterInProgress.getRiding(), ridingImageView);
    }

    public void increaseShootingValue() {
        increaseSkill(characterInProgress.getShooting(), shootingImageView);
    }
    public void reduceShootingValue() {
        reduceSkill(characterInProgress.getShooting(), shootingImageView);
    }

    public void increaseStealthValue() {
        increaseSkill(characterInProgress.getStealth(), stealthImageView);
    }
    public void reduceStealthValue() {
        reduceSkill(characterInProgress.getStealth(), stealthImageView);
    }

    public void increaseSwimmingValue() {
        increaseSkill(characterInProgress.getSwimming(), swimmingImageView);
    }
    public void reduceSwimmingValue() {
        reduceSkill(characterInProgress.getSwimming(), swimmingImageView);
    }

    public void increaseThrowingValue() {
        increaseSkill(characterInProgress.getThrowing(), throwingImageView);
    }
    public void reduceThrowingValue() {
        reduceSkill(characterInProgress.getThrowing(), throwingImageView);
    }

    public void increaseGamblingValue() {
        increaseSkill(characterInProgress.getGambling(), gamblingImageView);
    }
    public void reduceGamblingValue() {
        reduceSkill(characterInProgress.getGambling(), gamblingImageView);
    }

    public void increaseHealingValue() {
        increaseSkill(characterInProgress.getHealing(), healingImageView);
    }
    public void reduceHealingValue() {
        reduceSkill(characterInProgress.getHealing(), healingImageView);
    }

    public void increaseInvestigationValue() {
        increaseSkill(characterInProgress.getInvestigation(), investigationImageView);
    }
    public void reduceInvestigationValue() {
        reduceSkill(characterInProgress.getInvestigation(), investigationImageView);
    }

    public void increaseNoticeValue() {
        increaseSkill(characterInProgress.getNotice(), noticeImageView);
    }
    public void reduceNoticeValue() {
        reduceSkill(characterInProgress.getNotice(), noticeImageView);
    }

    public void increaseRepairValue() {
        increaseSkill(characterInProgress.getRepair(), repairImageView);
    }
    public void reduceRepairValue() {
        reduceSkill(characterInProgress.getRepair(), repairImageView);
    }

    public void increaseStreetwiseValue() {
        increaseSkill(characterInProgress.getStreetwise(), streetwiseImageView);
    }
    public void reduceStreetwiseValue() {
        reduceSkill(characterInProgress.getStreetwise(), streetwiseImageView);
    }

    public void increaseSurvivalValue() {
        increaseSkill(characterInProgress.getSurvival(), survivalImageView);
    }
    public void reduceSurvivalValue() {
        reduceSkill(characterInProgress.getSurvival(), survivalImageView);
    }

    public void increaseTauntValue() {
        increaseSkill(characterInProgress.getTaunt(), tauntImageView);
    }
    public void reduceTauntValue() {
        reduceSkill(characterInProgress.getTaunt(), tauntImageView);
    }

    public void increaseTrackingValue() {
        increaseSkill(characterInProgress.getTracking(), trackingImageView);
    }
    public void reduceTrackingValue() {
        reduceSkill(characterInProgress.getTracking(), trackingImageView);
    }

    public void increaseIntimidationValue() {
        increaseSkill(characterInProgress.getIntimidation(), intimidationImageView);
    }
    public void reduceIntimidationValue() {
        reduceSkill(characterInProgress.getIntimidation(), intimidationImageView);
    }

    public void increasePersuasionValue() {
        increaseSkill(characterInProgress.getStreetwise(), persuasionImageView);
    }
    public void reducePersuasionValue() {
        reduceSkill(characterInProgress.getPersuasion(), persuasionImageView);
    }

    public void increaseClimbingValue() {
        increaseSkill(characterInProgress.getClimbing(), climbingImageView);
    }
    public void reduceClimbingValue() {
        reduceSkill(characterInProgress.getClimbing(), climbingImageView);
    }



    private void adjustAttributePoints(int value) {
        characterCreatorSingleton.adjustAttributePoints(value);
        attributePointLabel.setText("Attribute Points: " + characterCreatorSingleton.getAttributePoints());

        if (characterCreatorSingleton.getAttributePoints() == 0) {
            skillBox1.setDisable(false);
            skillBox2.setDisable(false);
            skillPointLabel.setVisible(true);
            adjustSkillImageContrasts(1.0, 0.4);
        } else {
            adjustSkillImageContrasts(0.4, 0.8);
            resetSpentSkillPoints();
            skillBox1.setDisable(true);
            skillBox2.setDisable(true);
        }
    }

    private void resetSpentSkillPoints() {
        characterCreatorSingleton.setSkillPoints(15);
        skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());

        resetSkill(characterInProgress.getBoating(), boatingImageView);
        resetSkill(characterInProgress.getDriving(), drivingImageView);
        resetSkill(characterInProgress.getFighting(), fightingImageView);
        resetSkill(characterInProgress.getLockpicking(), lockpickingImageView);
        resetSkill(characterInProgress.getPiloting(), pilotingImageView);
        resetSkill(characterInProgress.getRiding(), ridingImageView);
        resetSkill(characterInProgress.getShooting(), shootingImageView);
        resetSkill(characterInProgress.getStealth(), stealthImageView);
        resetSkill(characterInProgress.getSwimming(), swimmingImageView);
        resetSkill(characterInProgress.getThrowing(), throwingImageView);
        resetSkill(characterInProgress.getGambling(), gamblingImageView);
        resetSkill(characterInProgress.getHealing(), healingImageView);
        resetSkill(characterInProgress.getInvestigation(), investigationImageView);
        resetSkill(characterInProgress.getNotice(), noticeImageView);
        resetSkill(characterInProgress.getRepair(), repairImageView);
        resetSkill(characterInProgress.getStreetwise(), streetwiseImageView);
        resetSkill(characterInProgress.getSurvival(), survivalImageView);
        resetSkill(characterInProgress.getTaunt(), tauntImageView);
        resetSkill(characterInProgress.getTracking(), trackingImageView);
        resetSkill(characterInProgress.getIntimidation(), intimidationImageView);
        resetSkill(characterInProgress.getPersuasion(), persuasionImageView);
        resetSkill(characterInProgress.getClimbing(), climbingImageView);
    }

    private void adjustSkillPoints(int value) {
        characterCreatorSingleton.adjustSkillPoints(value);
        skillPointLabel.setText("Skill Points: " + characterCreatorSingleton.getSkillPoints());

        if(characterCreatorSingleton.getSkillPoints() == 0) {
            nextButton.setDisable(false);
        } else {
            nextButton.setDisable(true);
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

        boatingImageView.setImage(imageMap.get(characterInProgress.getBoating().getValue()));
        drivingImageView.setImage(imageMap.get(characterInProgress.getDriving().getValue()));
        fightingImageView.setImage(imageMap.get(characterInProgress.getFighting().getValue()));
        lockpickingImageView.setImage(imageMap.get(characterInProgress.getLockpicking().getValue()));
        pilotingImageView.setImage(imageMap.get(characterInProgress.getPiloting().getValue()));
        ridingImageView.setImage(imageMap.get(characterInProgress.getRiding().getValue()));
        shootingImageView.setImage(imageMap.get(characterInProgress.getShooting().getValue()));
        stealthImageView.setImage(imageMap.get(characterInProgress.getStealth().getValue()));
        swimmingImageView.setImage(imageMap.get(characterInProgress.getSwimming().getValue()));
        throwingImageView.setImage(imageMap.get(characterInProgress.getThrowing().getValue()));
        gamblingImageView.setImage(imageMap.get(characterInProgress.getGambling().getValue()));
        healingImageView.setImage(imageMap.get(characterInProgress.getHealing().getValue()));
        investigationImageView.setImage(imageMap.get(characterInProgress.getInvestigation().getValue()));
        noticeImageView.setImage(imageMap.get(characterInProgress.getNotice().getValue()));
        repairImageView.setImage(imageMap.get(characterInProgress.getRepair().getValue()));
        streetwiseImageView.setImage(imageMap.get(characterInProgress.getStreetwise().getValue()));
        survivalImageView.setImage(imageMap.get(characterInProgress.getSurvival().getValue()));
        tauntImageView.setImage(imageMap.get(characterInProgress.getTaunt().getValue()));
        trackingImageView.setImage(imageMap.get(characterInProgress.getTracking().getValue()));
        intimidationImageView.setImage(imageMap.get(characterInProgress.getIntimidation().getValue()));
        persuasionImageView.setImage(imageMap.get(characterInProgress.getPersuasion().getValue()));
        climbingImageView.setImage(imageMap.get(characterInProgress.getClimbing().getValue()));

        if (characterCreatorSingleton.getSkillPoints() == 15) {
            adjustSkillImageContrasts(0.4, 0.8);
        } else {
            skillPointLabel.setVisible(true);
            skillBox1.setDisable(false);
            skillBox2.setDisable(false);
            if (characterCreatorSingleton.getSkillPoints() == 0) {
                nextButton.setDisable(false);
            }
        }

    }

    private void adjustSkillImageContrasts(double contrast, double brightness) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(contrast);
        colorAdjust.setBrightness(brightness);

        boatingImageView.setEffect(colorAdjust);
        drivingImageView.setEffect(colorAdjust);
        fightingImageView.setEffect(colorAdjust);
        lockpickingImageView.setEffect(colorAdjust);
        pilotingImageView.setEffect(colorAdjust);
        ridingImageView.setEffect(colorAdjust);
        shootingImageView.setEffect(colorAdjust);
        stealthImageView.setEffect(colorAdjust);
        swimmingImageView.setEffect(colorAdjust);
        throwingImageView.setEffect(colorAdjust);
        gamblingImageView.setEffect(colorAdjust);
        healingImageView.setEffect(colorAdjust);
        investigationImageView.setEffect(colorAdjust);
        noticeImageView.setEffect(colorAdjust);
        repairImageView.setEffect(colorAdjust);
        streetwiseImageView.setEffect(colorAdjust);
        survivalImageView.setEffect(colorAdjust);
        tauntImageView.setEffect(colorAdjust);
        trackingImageView.setEffect(colorAdjust);
        intimidationImageView.setEffect(colorAdjust);
        persuasionImageView.setEffect(colorAdjust);
        climbingImageView.setEffect(colorAdjust);
    }
}
