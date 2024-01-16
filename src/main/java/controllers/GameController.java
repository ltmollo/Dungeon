package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import maps.Map;
import players.Ability;
import players.Player;
import specialItems.Enemy;
import specialItems.SpecialElement;
import specialItems.SpecialItem;
import vectors.Direction;
import vectors.Vector2D;

import java.util.HashMap;

public class GameController {

    @FXML
    Button upBtn;
    @FXML
    Button rightBtn;
    @FXML
    Button leftBtn;

    @FXML
    Label strengthLabel;
    @FXML
    Label agilityLabel;
    @FXML
    Label armorLabel;
    @FXML
    Label luckLabel;
    @FXML
    Label experienceLabel;
    @FXML
    Label healthLabel;

    @FXML
    AnchorPane backgroundPane;
    @FXML
    GridPane moveGrid;
    @FXML
    AnchorPane mapPane;
    @FXML
    AnchorPane informationPane;

    @FXML
    HBox itemsBox;

    Rectangle specialRectangle;
    HashMap<Ability, Label> abilities = new HashMap<>();
    HashMap<Vector2D, Rectangle> encounteredRooms = new HashMap<>();
    Button takeBtn;

    Label informationLabel;

    private Map map;
    private Player player;

    private void setBackgroundImg(String imagePath) {
        Image image = new Image(imagePath);

        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize
        );

        Background background = new Background(backgroundImage);

        backgroundPane.setBackground(background);
    }

    @FXML
    protected void move() {
        Vector2D currentPosition = player.getPosition();
        Direction currentDirection = player.getDirection();

        Vector2D newPosition;

        switch (currentDirection) {
            case NORTH -> newPosition = new Vector2D(currentPosition.x, currentPosition.y + 1);
            case EAST -> newPosition = new Vector2D(currentPosition.x + 1, currentPosition.y);
            case SOUTH -> newPosition = new Vector2D(currentPosition.x, currentPosition.y - 1);
            case WEST -> newPosition = new Vector2D(currentPosition.x - 1, currentPosition.y);
            default -> newPosition = currentPosition;
        }

        player.setPosition(newPosition);

        String imgAfterMove = checkDoors(currentDirection, newPosition);

        setBackgroundImg(imgAfterMove);

        updateMap(currentPosition, newPosition);

    }

    private String checkDoors(Direction direction, Vector2D position) {
        String newUrl = "images/";
        newUrl += direction.getDisplayName();

        int[] result = map.checkDoorsInRoom(position);

        if (direction == Direction.NORTH) {
            newUrl += result[Direction.EAST.ordinal()];
            newUrl += result[Direction.WEST.ordinal()];
        } else if (direction == Direction.SOUTH) {
            newUrl += result[Direction.WEST.ordinal()];
            newUrl += result[Direction.EAST.ordinal()];
        }

        if (result[direction.ordinal()] == 1) {
            newUrl += "-doors.png";
            upBtn.setDisable(false);
            upBtn.setVisible(true);
        } else {
            newUrl += "-noDoors.png";
            upBtn.setDisable(true);
            upBtn.setVisible(false);
        }

        return newUrl;
    }

    @FXML
    public void rotate(ActionEvent event) {

        Object source = event.getSource();
        Button clickedButton = (Button) source;
        String buttonId = clickedButton.getId();

        Direction newDirection;
        String imgAfterRotation;

        switch (buttonId) {
            case ("rightBtn") -> {
                newDirection = player.getDirection().rotateClockwise();
                player.changeDirection(newDirection);
            }
            case ("leftBtn") -> {
                newDirection = player.getDirection().rotateCounterClockwise();
                player.changeDirection(newDirection);
            }
            default -> newDirection = null;
        }
        assert newDirection != null;
        imgAfterRotation = checkDoors(newDirection, player.getPosition());

        setDirectionOnMap();
        setBackgroundImg(imgAfterRotation);
    }


    private void addRectangleToMap(Vector2D position) {
        double width = 15;
        double height = 15;

        Rectangle rectangle = new Rectangle(width, height, Color.WHITE);
        rectangle.getStyleClass().add("mapRectangle");

        encounteredRooms.put(position, rectangle);
        mapPane.getChildren().add(rectangle);

        AnchorPane.setBottomAnchor(rectangle, 1.25 * height + position.y * height);
        AnchorPane.setLeftAnchor(rectangle, 2 * width + position.x * width);

        setDirectionOnMap();
    }

    private void addNeighboursOnTheMap(Vector2D position) {
        Vector2D[] neighboursTable = map.neighboursRooms(position);
        for (Vector2D neighbourPosition : neighboursTable) {
            if (neighbourPosition == null || encounteredRooms.containsKey(neighbourPosition)) {
                continue;
            }
            addRectangleToMap(neighbourPosition);
        }
    }

    private void updateMap(Vector2D oldPosition, Vector2D newPosition) {
        Rectangle oldPlaceOnMap = encounteredRooms.get(oldPosition);
        oldPlaceOnMap.setFill(Color.GRAY);

        if (encounteredRooms.containsKey(newPosition)) {
            Rectangle oldRectangle = encounteredRooms.get(newPosition);
            oldRectangle.setFill(Color.WHITE);
            setDirectionOnMap();

        } else {
            addRectangleToMap(newPosition);
        }
        addNeighboursOnTheMap(newPosition);

        backgroundPane.getChildren().remove(takeBtn);
        backgroundPane.getChildren().remove(informationLabel);

        SpecialItem specialItem = map.checkRoom(newPosition);
        addSpecialElement(newPosition, specialItem);
    }

    private void addSpecialElement(Vector2D position, SpecialItem specialItem) {
        if (specialItem == null) {
            specialRectangle.setVisible(false);
            return;
        }

        String imagePath = specialItem.getImagePath();
        int width = specialItem.getWidth();
        int height = specialItem.getHeight();

        encounterItem(specialItem, position);
        specialRectangle.setVisible(true);
        specialRectangle.setWidth(width);
        specialRectangle.setHeight(height);

        AnchorPane.setTopAnchor(specialRectangle, (backgroundPane.getHeight() - specialRectangle.getHeight()) / 2);
        AnchorPane.setLeftAnchor(specialRectangle, (backgroundPane.getWidth() - specialRectangle.getWidth()) / 2);

        setRectangleImage(specialRectangle, imagePath);
    }

    private void setRectangleImage(Rectangle rectangle, String imagePath) {
        Image backgroundImage = new Image(imagePath);
        ImagePattern img = new ImagePattern(backgroundImage);
        rectangle.setFill(img);
    }

    private void setDirectionOnMap() {
        Rectangle rectangle = encounteredRooms.get(player.getPosition());
        String imagePath = "images/^" + player.getDirection().ordinal() + ".png";
        setRectangleImage(rectangle, imagePath);
    }

    private void initializeSpecialRectangle() {
        this.specialRectangle = new Rectangle(100, 100);
        this.specialRectangle.getStyleClass().add("specialRectangle");
        this.specialRectangle.setVisible(false);
        backgroundPane.getChildren().add(specialRectangle);
    }

    private void initializePlayerDetails() {

        abilities.put(Ability.HEALTH, healthLabel);
        abilities.put(Ability.STRENGTH, strengthLabel);
        abilities.put(Ability.ARMOR, armorLabel);
        abilities.put(Ability.AGILITY, agilityLabel);
        abilities.put(Ability.LUCK, luckLabel);
        abilities.put(Ability.EXPERIENCE, experienceLabel);

        updatePlayerDetails();
    }

    private void updatePlayerDetails() {
        HashMap<Ability, Integer> playerDetails = player.getAbilities();

        for (Ability ability : abilities.keySet()) {
            Label label = abilities.get(ability);
            label.setText(ability.getDisplayName() + ": " + playerDetails.get(ability));
        }
    }

    private void encounterItem(SpecialItem specialItem, Vector2D position) {
        takeBtn.setOnAction(e -> {
            takeItem(specialItem, position);
            backgroundPane.getChildren().remove(takeBtn);
        });

        if (specialItem.getSpecialElement() == SpecialElement.TREASURE) {
            if (!player.getPickedItems().containsKey(SpecialElement.KEY)) {
                informationLabel.setText("You need a key\nto open a chest");
                backgroundPane.getChildren().add(informationLabel);

                AnchorPane.setLeftAnchor(informationLabel, (backgroundPane.getWidth() - informationLabel.getWidth()) / 2);
                AnchorPane.setBottomAnchor(informationLabel, 20.0);
                return;
            } else {
                takeBtn.setText("Open");
            }
        } else if (specialItem.getSpecialElement() == SpecialElement.MONSTER) {
            takeBtn.setText("Fight");
            takeBtn.setOnAction(e -> {
                fight((Enemy) specialItem, position);
                backgroundPane.getChildren().remove(takeBtn);
            });
        } else {
            takeBtn.setText("Take");
        }

        backgroundPane.getChildren().add(takeBtn);

        AnchorPane.setBottomAnchor(takeBtn, 40.0);
        AnchorPane.setLeftAnchor(takeBtn, (backgroundPane.getWidth() - takeBtn.getWidth()) / 2);
    }

    private void fight(Enemy monster, Vector2D position) {
        do {
            int hitPlayer = player.strike();
            int taken = monster.takeDamage(hitPlayer);
            System.out.println(hitPlayer + " " + taken);
            int hitMonster = monster.strike();
            player.takeDamage(hitMonster);
            updatePlayerDetails();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!player.isDead() && !monster.isDead());

        specialRectangle.setVisible(false);
        map.deleteFromRoom(position);
    }

    private void takeItem(SpecialItem specialItem, Vector2D position) {
        HashMap<Ability, Integer> rewards = specialItem.getRewards();
        player.updateAbilities(rewards);
        player.pickItem(specialItem);

        updatePlayerDetails();
        specialRectangle.setVisible(false);

        map.deleteFromRoom(position);

        if (specialItem.getSpecialElement() != SpecialElement.MONSTER) {
            updateItemBox(specialItem);
        }
    }

    private void initializeTakeBtn() {
        takeBtn = new Button("Take");
        takeBtn.getStyleClass().add("takeBtn");
    }

    private void initializeInformationLabel() {
        informationLabel = new Label();
        informationLabel.getStyleClass().add("information");
    }

    private void updateItemBox(SpecialItem specialItem) {
        Rectangle rectangle = new Rectangle(25, 25);
        rectangle.getStyleClass().add("itemPicked");
        setRectangleImage(rectangle, specialItem.getImagePath());
        itemsBox.getChildren().add(rectangle);

    }

    @FXML
    public void setModel(Player player, Map map) {
        this.player = player;
        this.map = map;
        setBackgroundImg("images/N00-doors.png");
        AnchorPane.setBottomAnchor(moveGrid, 0.0);
        AnchorPane.setRightAnchor(moveGrid, 0.0);

        AnchorPane.setTopAnchor(mapPane, 20.0);
        AnchorPane.setLeftAnchor(mapPane, 20.0);

        AnchorPane.setLeftAnchor(informationPane, 0.0);
        AnchorPane.setBottomAnchor(informationPane, 0.0);

        addRectangleToMap(player.getPosition());
        addNeighboursOnTheMap(player.getPosition());

        initializeSpecialRectangle();
        initializePlayerDetails();
        initializeTakeBtn();
        initializeInformationLabel();
    }
}
