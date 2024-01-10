package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import maps.Map;
import players.Player;
import vectors.Direction;
import vectors.Vector2D;

import javax.annotation.processing.Generated;
import javax.swing.text.Position;
import java.util.Vector;

public class GameController {

    @FXML
    VBox backgroundVBox;
    @FXML
    Button rightBtn;
    @FXML
    Button leftBtn;

    private Map map;
    private Player player;

    private void setBackgroundVBox(String imagePath){
        Image image = new Image(imagePath);

        // Tworzenie obiektu BackgroundImage
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        Background background = new Background(backgroundImage);

        backgroundVBox.setBackground(background);
    }

    @FXML
    protected void move(){
        Vector2D currentPosition = player.getPosition();
        Direction currentDirection = player.getDirection();

        Vector2D newPosition;

        switch (currentDirection){
            case NORTH -> newPosition = new Vector2D(currentPosition.x, currentPosition.y + 1);
            case EAST -> newPosition = new Vector2D(currentPosition.x + 1, currentPosition.y);
            case SOUTH -> newPosition = new Vector2D(currentPosition.x, currentPosition.y - 1);
            case WEST -> newPosition = new Vector2D(currentPosition.x - 1, currentPosition.y);
            default -> newPosition = null;
        }

        player.setPosition(newPosition);

        String imgAfterMove = checkDoors(currentDirection, newPosition);

        setBackgroundVBox(imgAfterMove);
    }

    private String checkDoors(Direction direction, Vector2D position){
        String newUrl = "images/";
        newUrl += direction.getDisplayName();

        int[] result = map.checkDoorsInRoom(position);

        if (direction == Direction.NORTH || direction == Direction.SOUTH){
            newUrl += result[Direction.EAST.ordinal()];
            newUrl += result[Direction.WEST.ordinal()];
        }
        if (result[direction.ordinal()] == 1){
            newUrl += "-doors.png";
        }else{
            newUrl += "-noDoors.png";
        }

        return newUrl;

    }
    @FXML
    public void rotate(ActionEvent event){

        Object source = event.getSource();
        Button clickedButton = (Button) source;
        String buttonId = clickedButton.getId();

        Direction newDirection;
        String imgAfterRotation;

        switch (buttonId){
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

        setBackgroundVBox(imgAfterRotation);
    }

    @FXML
    public void setModel(Player player, Map map){
        this.player = player;
        this.map = map;
    }

}
