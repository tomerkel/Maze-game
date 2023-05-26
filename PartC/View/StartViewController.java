package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * this class is the controller for the first scene
 */
public class StartViewController {
    public static Scene GameScene;
    public static Stage PrimaryStage;
    public static MyViewController GameSceneController;


    public void StartButtonHandle(ActionEvent actionEvent) {
        GameSceneController.setOnOpenTheScene();
        PrimaryStage.setScene(GameScene);
    }

    public void ExitButtonHandle(ActionEvent actionEvent) {
        GameSceneController.ExitButtonHandle();
        Platform.exit();
    }

    public void SettingsButtonHandle(ActionEvent actionEvent) {
        GameSceneController.PropertiesHandle();
    }

    public void LoadButtonHandle(ActionEvent actionEvent) {
        GameSceneController.LoadButtonHandle(actionEvent);
        GameSceneController.setOnOpenTheScene();
        PrimaryStage.setScene(GameScene);
    }
}
