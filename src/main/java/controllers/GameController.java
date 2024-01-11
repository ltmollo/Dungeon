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
            case ("rightBtn"):
                newDirection = player.getDirection().rotateClockwise();
                player.changeDirection(newDirection);
                break;
            case ("leftBtn"):
                newDirection = player.getDirection().rotateCounterClockwise();
                player.changeDirection(newDirection);
                break;
            default:
                newDirection = null;
        }
        imgAfterRotation = checkDoors(newDirection, player.getPosition());
        setRotation();

        setBackgroundImg(imgAfterRotation);
    }


    private void addRectangleToMap(Vector2D beginPosition) {
        double width = 15;
        double height = 15;

        Rectangle rectangle = new Rectangle(width, height, Color.WHITE);
        rectangle.getStyleClass().add("mapRectangle");

        encounteredRooms.put(beginPosition, rectangle);
        mapPane.getChildren().add(rectangle);
        setRotation();

        AnchorPane.setBottomAnchor(rectangle, 2 * height + beginPosition.y * height);
        AnchorPane.setLeftAnchor(rectangle, 2 * width + beginPosition.x * width);

    }

    private void updateMap(Vector2D oldPosition, Vector2D newPosition) {
        Rectangle oldPlaceOnMap = encounteredRooms.get(oldPosition);
        oldPlaceOnMap.setFill(Color.GRAY);

        if (encounteredRooms.containsKey(newPosition)) {
            Rectangle oldRectangle = encounteredRooms.get(newPosition);
            oldRectangle.setFill(Color.WHITE);
            setRotation();

        } else {
            addRectangleToMap(newPosition);
        }
    }

    private void setRotation() {
        Rectangle rectangle = encounteredRooms.get(player.getPosition());
        String imagePath = "images/^" + player.getDirection().ordinal() + ".png";
        Image backgroundImage = new Image(imagePath);
        ImagePattern img = new ImagePattern(backgroundImage);
        rectangle.setFill(img);
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
    }

}
