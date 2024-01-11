package dungeonApp;

import controllers.GameController;
import controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import maps.Map;
import players.Player;


public class DungeonApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            this.stage = stage;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DungeonApp.class.getClassLoader().getResource("views/menu.fxml"));

            VBox rootLayout = loader.load();
            MenuController menuController = loader.getController();
            menuController.setModel(this);

            configureStage(rootLayout);
            stage.show();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void startGame(Player player) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DungeonApp.class.getClassLoader().getResource("views/game.fxml"));

            AnchorPane rootLayout = loader.load();
            GameController gameController = loader.getController();
            gameController.setModel(player, new Map());

            configureStage2(rootLayout);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void configureStage2(AnchorPane rootLayout) {
        Scene scene = new Scene(rootLayout, 900, 700);

        this.stage.setScene(scene);
        this.stage.setTitle("Dungeon");
    }


    private void configureStage(VBox rootLayout) {
        Scene scene = new Scene(rootLayout, 900, 700);

        this.stage.setScene(scene);
        this.stage.setTitle("Dungeon");
    }
}