package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main extends Application
{

    @Override
    /**
     * the base of the application
     */
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoaderStart = new FXMLLoader(getClass().getResource("../View/StartWindow.fxml"));
        Parent rootStart = fxmlLoaderStart.load();
        FXMLLoader fxmlLoaderMain = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent rootMain = fxmlLoaderMain.load();
        StartViewController StartView = fxmlLoaderStart.getController();
        MyViewController view = fxmlLoaderMain.getController();

        Scene startScene = new Scene(rootStart, 720, 440);
        Scene GameScene = new Scene(rootMain,950,740);
        // connecting the start scene to the main application scene.
        view.thisScene = GameScene;
        StartView.GameSceneController = view;
        view.PrimaryStage = primaryStage;
        StartView.PrimaryStage = primaryStage;
        view.startScene = startScene;
        StartView.GameScene = GameScene;
        primaryStage.setOnCloseRequest(e -> view.ExitButtonHandle());
        primaryStage.setTitle("MazeApp");
        primaryStage.setScene(view.startScene);
        // change the game icon
        Image IconImageMazeApp = null;
        try{
            IconImageMazeApp = new Image(new FileInputStream("./src/Sources/Images/pallbearer.jpg"));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        primaryStage.getIcons().add(IconImageMazeApp);

        // MVVM design pattern
        primaryStage.show();
        IModel model = new MyModel();
        model.startAndSetServer();
        MyViewModel VM = new MyViewModel(model);
        view.SetViewModel(VM);
        VM.addObserver(view);




    }
    public static void main(String[] args) {
        launch(args);
    }
}
