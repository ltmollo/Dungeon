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
import maps.SpecialElement;
import players.Player;
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
    Label positionLabel;

    @FXML
    AnchorPane backgroundPane;
    @FXML
    GridPane moveGrid;
    @FXML
    AnchorPane mapPane;

    Rectangle specialRectangle;

    HashMap<Vector2D, Rectangle> encounteredRooms = new HashMap<>();

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
            default -> newPosition = null;
        }

        player.setPosition(newPosition);

        String imgAfterMove = checkDoors(currentDirection, newPosition);

        setBackgroundImg(imgAfterMove);

        positionLabel.setText(newPosition.toString() + " " + currentDirection);
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

        AnchorPane.setBottomAnchor(rectangle, 2 * height + position.y * height);
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

        SpecialElement specialElement = map.checkRoom(newPosition);
        addSpecialElement(newPosition, specialElement);
    }

    private void addSpecialElement(Vector2D position, SpecialElement specialElement){
        String imagePath;
        int width = 200;
        int height = 200;

        if( specialElement == null) {
            specialRectangle.setVisible(false);
            return;
        }
        switch (specialElement) {
            case MONSTER -> {
                imagePath = "images/monster.png";
                width = 400;
                height = 400;
            }

            case KEY -> {
                    imagePath = "images/key.png";
                    width = 500;
            }
            case SWORD -> {
                imagePath = "images/sword.png";
                height = 500;
            }

            case HELMET -> imagePath = "images/helmet.png";

            case TREASURE -> imagePath = "images/chest.png";

            case ARMOR -> imagePath = "images/armor.png";

            default -> imagePath = null;
        }
        if (imagePath == null ) {
            return;
        }
        specialRectangle.setVisible(true);
        specialRectangle.setWidth(width);
        specialRectangle.setHeight(height);

        AnchorPane.setTopAnchor(specialRectangle, (backgroundPane.getHeight() - specialRectangle.getHeight()) / 2);
        AnchorPane.setLeftAnchor(specialRectangle, (backgroundPane.getWidth() - specialRectangle.getWidth()) / 2);

        setRectangleImage(specialRectangle, imagePath);
    }

    private void setRectangleImage(Rectangle rectangle, String imagePath){
        Image backgroundImage = new Image(imagePath);
        ImagePattern img = new ImagePattern(backgroundImage);
        rectangle.setFill(img);
    }

    private void setDirectionOnMap() {
        Rectangle rectangle = encounteredRooms.get(player.getPosition());
        String imagePath = "images/^" + player.getDirection().ordinal() + ".png";
        setRectangleImage(rectangle, imagePath);
    }

    private void initializeSpecialRectangle(){
        this.specialRectangle = new Rectangle(100, 100);
        this.specialRectangle.getStyleClass().add("specialRectangle");
        this.specialRectangle.setVisible(false);
        backgroundPane.getChildren().add(specialRectangle);
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

        addRectangleToMap(player.getPosition());
        addNeighboursOnTheMap(player.getPosition());

        initializeSpecialRectangle();

    }
}
