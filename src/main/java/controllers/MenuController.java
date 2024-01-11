package controllers;

import dungeonApp.DungeonApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import players.Player;

public class MenuController {

    @FXML
    private Label strengthLabel;
    @FXML
    private Label agilityLabel;
    @FXML
    private Label informationLabel;
    @FXML
    private Label luckLabel;
    @FXML
    Label armorLabel;

    @FXML
    private Button increaseStrengthBtn;
    @FXML
    private Button increaseAgilityBtn;
    @FXML
    private Button increaseLuckBtn;
    @FXML
    private Button increaseArmorBtn;

    @FXML
    public Button decreaseStrengthBtn;
    @FXML
    public Button decreaseAgilityBtn;
    @FXML
    private Button decreaseLuckBtn;
    @FXML
    private Button decreaseArmorBtn;

    @FXML
    private Button submitBtn;

    private int pointsRemaining = 10;
    private int strengthInt = 10;
    private int agilityInt = 10;
    private int luckInt = 10;
    private int armorInt = 10;
    private DungeonApp dungeonApp;

    @FXML
    protected void increase(ActionEvent event) {
        if (pointsRemaining == 0) {
            informationLabel.setText("You don't have more points to give");
            return;
        }

        Object source = event.getSource();
        Button clickedButton = (Button) source;
        String buttonId = clickedButton.getId();
        switch (buttonId) {
            case ("increaseStrengthBtn"):
                strengthInt++;

                strengthLabel.setText("Strength " + strengthInt);
                decreaseStrengthBtn.setDisable(false);
                if (strengthInt == 15) {
                    increaseStrengthBtn.setDisable(true);
                }
                break;
            case ("increaseAgilityBtn"):
                agilityInt++;

                agilityLabel.setText("Agility " + agilityInt);
                decreaseAgilityBtn.setDisable(false);
                if (agilityInt == 15) {
                    increaseAgilityBtn.setDisable(true);
                }
                break;
            case ("increaseLuckBtn"):
                luckInt++;

                luckLabel.setText("Luck " + luckInt);
                decreaseLuckBtn.setDisable(false);
                if (luckInt == 15) {
                    increaseLuckBtn.setDisable(true);
                }
                break;
            case ("increaseArmorBtn"):
                armorInt++;

                armorLabel.setText("Armor " + armorInt);
                decreaseArmorBtn.setDisable(false);
                if (armorInt == 15) {
                    increaseArmorBtn.setDisable(true);
                }
                break;
        }
        pointsRemaining--;
        informationLabel.setText("Points to spend " + pointsRemaining);
    }

    @FXML
    protected void decrease(ActionEvent event) {
        if (pointsRemaining == 10) {
            informationLabel.setText("You can't deny more points");
            return;
        }

        Object source = event.getSource();
        Button clickedButton = (Button) source;
        String buttonId = clickedButton.getId();
        switch (buttonId) {
            case ("decreaseStrengthBtn"):
                strengthInt--;
                strengthLabel.setText("Strength " + strengthInt);
                increaseStrengthBtn.setDisable(false);
                if (strengthInt == 10) {
                    decreaseStrengthBtn.setDisable(true);
                }
                break;

            case ("decreaseAgilityBtn"):
                agilityInt--;
                agilityLabel.setText("Agility " + agilityInt);
                increaseAgilityBtn.setDisable(false);
                if (agilityInt == 10) {
                    decreaseAgilityBtn.setDisable(true);
                }
                break;

            case ("decreaseArmorBtn"):
                armorInt--;
                armorLabel.setText("Armor " + armorInt);
                increaseArmorBtn.setDisable(false);
                if (armorInt == 10) {
                    decreaseArmorBtn.setDisable(true);
                }
                break;

            case ("decreaseLuckBtn"):
                luckInt--;
                luckLabel.setText("Luck " + luckInt);
                increaseLuckBtn.setDisable(false);
                if (luckInt == 10) {
                    decreaseLuckBtn.setDisable(true);
                }
                break;
        }
        pointsRemaining++;
        informationLabel.setText("Points to spend " + pointsRemaining);
    }

    public void setModel(DungeonApp dungeonApp) {
        this.dungeonApp = dungeonApp;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void submit() throws Exception {
        Player player = new Player(armorInt, agilityInt, luckInt, strengthInt);
        this.dungeonApp.startGame(player);
    }
}