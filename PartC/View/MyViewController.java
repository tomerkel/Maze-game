package View;

import ViewModel.MyViewModel;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * this class is the main controller of the app
 */
public class MyViewController implements IView, Observer {
    public static MyViewModel viewModel;
    static MediaPlayer MP;
    public static double volumePower = 0.3;
    static String MusicRetroVersion = "C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\Music\\AstronomiaCoffinDance80sremix.mp3";
    static String MusicRemixVersion = "C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\Music\\AstronomiaCoffinDanceRemix.mp3" ;
    static String MusicOriginalVersion = "C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\Music\\AstronomiaCoffinDance.mp3" ;
    static String MusicGuitarVersion = "C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\Music\\AstronomiaCoffinDanceGuitar.mp3";
    static String MusicAcousticVersion = "C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\Music\\AstronomiaCoffinDancePianoCover.mp3" ;
    static String playerImageString = "./src/Sources/MazeImages/coffinDancePlayer.png" ;
    static String wallImageString = "./src/Sources/MazeImages/CoffinDanceWall.jpg" ;
    static String GoalImageString = "./src/Sources/MazeImages/CoffinDanceGoal.jpg" ;
    static String SolImageString = "./src/Sources/MazeImages/SolImage.png" ;
    static String AlertClipString = new File("src/Sources/Video/CoffinDanceClip.mp4").getAbsolutePath();
    public static Scene startScene;
    public static Scene thisScene;
    public static Stage PrimaryStage;
    public static FileChooser SaveFileChooser = new FileChooser();
    public static FileChooser LoadFileChooser = new FileChooser();

    @FXML
    public RadioMenuItem PlayMusicButton;
    @FXML
    public Label ValidNumberLabel;
    @FXML
    public TextField TextFieldRow;
    @FXML
    public TextField TextFieldCol;
    @FXML
    public MazeDisplayer mazeDisplayerCanvas;
    @FXML
    public Button SolveMazeButton;
    @FXML
    public Button GenerateButton;
    @FXML
    public MenuItem SaveMazeButton;
    @FXML
    public BorderPane BorderPaneAPP;
    @FXML
    public AnchorPane AnchorPaneLeftAPP;
    @FXML
    public MenuBar menuBar;
    @FXML
    public AnchorPane AnchorPaneAPP;
    @FXML
    public Pane PaneOfMaze;

    @FXML
    public ImageView MenubarMovementImage;

    private void zoomInOutHandle(ScrollEvent event){
        if(viewModel.getMaze() != null){
            if (event.isControlDown()) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();

                if(deltaY < 0){
                    zoomFactor = 2 - zoomFactor;
                }

                PaneOfMaze.setScaleX(PaneOfMaze.getScaleX() * zoomFactor);
                PaneOfMaze.setScaleY(PaneOfMaze.getScaleY() * zoomFactor);

            }
        }
    }
    //########## Start of Stage & Scene ##########//
    public void setOnOpenTheScene(){
        PaneOfMaze.prefWidthProperty().bind(BorderPaneAPP.widthProperty());
        PaneOfMaze.prefHeightProperty().bind(BorderPaneAPP.heightProperty());
        this.mazeDisplayerCanvas.getGraphicsContext2D().getCanvas().widthProperty().bind(PaneOfMaze.widthProperty());
        this.mazeDisplayerCanvas.getGraphicsContext2D().getCanvas().heightProperty().bind(PaneOfMaze.heightProperty());
        BorderPaneAPP.prefWidthProperty().bind(AnchorPaneAPP.widthProperty());
        BorderPaneAPP.prefHeightProperty().bind(AnchorPaneAPP.heightProperty());

        PrimaryStage.widthProperty().addListener((obs,oldVal,newVal) ->{
            setOnSizeChange();
        });
        PrimaryStage.heightProperty().addListener((obs,oldVal,newVal) ->{
            setOnSizeChange();
        });
        PaneOfMaze.widthProperty().addListener((obs,oldVal,newVal) ->{
            setOnSizeChange();
        });
        PaneOfMaze.heightProperty().addListener((obs,oldVal,newVal) ->{
            setOnSizeChange();
        });

        //the music handle on open the main scene
        Media Music = new Media(new File(this.MusicRetroVersion).toURI().toString());
        this.MP = new MediaPlayer(Music);
        this.MP.setVolume(this.volumePower);
        this.MP.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                MP.seek(Duration.ZERO);
            }
        });
        this.MP.setAutoPlay(true);
        //this will handle the moving image on the menu bar
        Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new CubicCurveTo(240,0,400,0,620,0));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(MenubarMovementImage);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
    }

    private void setOnSizeChange(){
        if (mazeDisplayerCanvas.getMaze() != null) {
            mazeDisplayerCanvas.drawMaze();
            mazeDisplayerCanvas.setGoalPosition(mazeDisplayerCanvas.getGoalRow(),mazeDisplayerCanvas.getGoalCol());
            mazeDisplayerCanvas.setPlayerPosition(mazeDisplayerCanvas.getCurrRowPlayer(),mazeDisplayerCanvas.getCurrColPlayer());
            if(mazeDisplayerCanvas.getPathOfSol() != null){
                mazeDisplayerCanvas.setMazeSolutionPath(viewModel.getSolution());
                mazeDisplayerCanvas.drawMazeSolution();
            }
        }
    }
    public void SetViewModel(MyViewModel ViewModel ) {
        this.viewModel = ViewModel;
    }

    //########## Update ##########//
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
            String changeString = (String)arg;
            switch(changeString){
                case "input is not valid":
                    ValidNumberLabel.setVisible(true);
                    TextFieldRow.clear();
                    TextFieldCol.clear();
                    break;
                case "maze generated": case "maze was loaded":
                    mazeDisplayerCanvas.setPathOfSol(null);
                    mazeDisplayerCanvas.setImageNamePlayer(playerImageString);
                    mazeDisplayerCanvas.setImageNameWall(wallImageString);
                    mazeDisplayerCanvas.setImageNameGoal(GoalImageString);
                    mazeDisplayerCanvas.setImageNameSol(SolImageString);
                    mazeDisplayerCanvas.setMaze(viewModel.getMazeAsArray());
                    mazeDisplayerCanvas.drawMaze();
                    mazeDisplayerCanvas.setGoalPosition(2*viewModel.getMaze().getGoalPosition().getRowIndex(),2*viewModel.getMaze().getGoalPosition().getColumnIndex());
                    mazeDisplayerCanvas.setPlayerPosition(2*viewModel.getMaze().getStartPosition().getRowIndex(),2*viewModel.getMaze().getStartPosition().getColumnIndex());
                    ValidNumberLabel.setVisible(false);
                    SolveMazeButton.setDisable(false);
                    SolveMazeButton.setText("Solve Maze");
                    break;
                case "maze solved":
                    mazeDisplayerCanvas.setMazeSolutionPath(viewModel.getSolution());
                    mazeDisplayerCanvas.drawMazeSolution();
                    break;
                case "player moved":
                    mazeDisplayerCanvas.setPlayerPosition(viewModel.getRowPlayer(),viewModel.getColPlayer());
                    break;
                case "you made it to the goal!":
                    mazeDisplayerCanvas.setPlayerPosition(viewModel.getRowPlayer(),viewModel.getColPlayer());
                    ButtonType newGameButtonType = new ButtonType("New Game" , ButtonBar.ButtonData.LEFT);
                    ButtonType noButtonType = new ButtonType("No");
                    Alert a = new Alert(Alert.AlertType.NONE,"",newGameButtonType,noButtonType);
                    a.getDialogPane().setStyle("-fx-background-color: black;");
                    Button newGameButton = (Button) a.getDialogPane().lookupButton(newGameButtonType);
                    newGameButton.setAlignment(Pos.CENTER);
                    a.setTitle("YOU WIN !!!");
                    a.setHeaderText(null);
                    Media vid =  new Media(new File(AlertClipString).toURI().toString());
                    MediaPlayer player = new MediaPlayer(vid);
                    player.setVolume(0.05);
                    MediaView mediaView = new MediaView(player);
                    mediaView.setFitHeight(400);
                    mediaView.setFitWidth(600);
                    VBox content = new VBox(0.001, mediaView);
                    player.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            player.seek(Duration.ZERO);
                        }
                    });
                    content.setAlignment(Pos.CENTER);
                    a.getDialogPane().setContent(content);
                    MediaPlayer.Status MusicStatus = MP.getStatus();
                    a.setOnShowing(e -> {MP.pause();player.play();});
                    a.getDialogPane().setPrefHeight(420);
                    a.getDialogPane().setPrefWidth(610);
                    a.getDialogPane().getScene().getWindow().setOnCloseRequest(e-> {a.close();});
                    Optional<ButtonType> result = a.showAndWait();
                    if(!result.isPresent()){
                        player.stop();
                        MusicSwitchCase(MusicStatus);
                    }
                    else if(result.get() == newGameButtonType){
                        player.stop();
                        MusicSwitchCase(MusicStatus);
                        NewButtonHandle(new ActionEvent());
                    }
                    else if(result.get() == noButtonType){
                        player.stop();
                        MusicSwitchCase(MusicStatus);
                    }
                    break;
                case "Servers are close":
                    Platform.exit();
                    break;
            }
        }
    }

    //########## Music Handler ##########//
    private void changeMusic(String SongToPlay){
        StopMusicButtonHandle();
        PlayMusicButton.setSelected(true);
        Media Music = new Media(new File(SongToPlay).toURI().toString());
        MP = new MediaPlayer(Music);
        onEndOfMediaRestart();
        MP.setVolume(volumePower);
        MP.setAutoPlay(true);
    }
    private void MusicSwitchCase(MediaPlayer.Status MusicStatus){
        switch (MusicStatus){
            case PAUSED:
                PauseMusicButtonHandle();
                break;
            case STOPPED:
                StopMusicButtonHandle();
                break;
            case PLAYING:
                PlayMusicButtonHandle();
                break;
        }
    }
    public void onEndOfMediaRestart(){
        MP.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                MP.seek(Duration.ZERO);
            }
        });
    }
    public void ChangeToRetro(){
        changeMusic(MusicRetroVersion);
    }
    public void ChangeToRemix(){
        changeMusic(MusicRemixVersion);
    }
    public void ChangeToOriginal(){
        changeMusic(MusicOriginalVersion);
    }
    public void ChangeToGuitar(){
        changeMusic(MusicGuitarVersion);
    }
    public void ChangeToAcoustic(){
        changeMusic(MusicAcousticVersion);
    }
    public void PlayMusicButtonHandle(){
        MP.play();
    }
    public void PauseMusicButtonHandle(){
        MP.pause();
    }
    public void StopMusicButtonHandle(){
        MP.stop();
    }
    public void VolumeUpButtonHandle(){
        if(volumePower + 0.05 <= 1.0){
            volumePower += 0.05;
            MP.setVolume(volumePower);
        }
    }
    public void VolumeDownButtonHandle(){
        if(volumePower - 0.05 >= 0.0){
            volumePower -= 0.05;
            MP.setVolume(volumePower);
        }
    }

    //########## Generate and Solve Handler ##########//
    public void GenerateMazeButtonHandle(ActionEvent actionEvent) {
        String rows = TextFieldRow.getText();
        String cols = TextFieldCol.getText();
        viewModel.GenerateMaze(rows,cols);
        SaveMazeButton.setVisible(true);

    }
    public void SolveMazeButtonHandle(ActionEvent actionEvent) {
        switch (SolveMazeButton.getText()){
            case "Solve Maze":
                viewModel.solveMaze();
                SolveMazeButton.setText("Hide");
                break;
            case "Hide":
                mazeDisplayerCanvas.CanvasToBasic();
                mazeDisplayerCanvas.drawMaze();
                mazeDisplayerCanvas.setGoalPosition(mazeDisplayerCanvas.getGoalRow(), mazeDisplayerCanvas.getGoalCol());
                mazeDisplayerCanvas.setPlayerPosition(mazeDisplayerCanvas.getCurrRowPlayer(),mazeDisplayerCanvas.getCurrColPlayer());
                mazeDisplayerCanvas.setPathOfSol(null);
                SolveMazeButton.setText("Solve Maze");
                break;
        }


    }

    //########## MazeDisplayer key and mouse event Handler ##########//
    public void keyPressedHandle(KeyEvent keyEvent) {
        if(mazeDisplayerCanvas.getMaze() != null){
            viewModel.movePlayer(keyEvent);
            keyEvent.consume();
        }
    }
    public void MazeDisplayerOnMouseClickHandle(MouseEvent mouseEvent) {
        if(mazeDisplayerCanvas.getMaze() != null){
            mazeDisplayerCanvas.requestFocus();
        }
    }

    //########## Save & Load & New Handler ##########//
    public void SaveButtonHandle(ActionEvent actionEvent) {
        SaveFileChooser.setInitialDirectory(new File("C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\SavedMaze"));
        Window stage = SolveMazeButton.getScene().getWindow();
        SaveFileChooser.setTitle("Save Your Maze");
        SaveFileChooser.setInitialFileName("MyMaze");
        SaveFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze (*.File)","*"));
        File file = SaveFileChooser.showSaveDialog(stage);
        viewModel.SaveMaze(file);

    }
    public void LoadButtonHandle(ActionEvent actionEvent) {
        LoadFileChooser.setInitialDirectory(new File("C:\\Users\\tomer\\IdeaProjects\\ATP-Project-PartC\\src\\Sources\\SavedMaze"));
        Window stage = SolveMazeButton.getScene().getWindow();
        LoadFileChooser.setTitle("Load Your Maze");
        LoadFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze (*.File)","*"));
        File file = LoadFileChooser.showOpenDialog(stage);
        viewModel.LoadMaze(file);

    }
    public void NewButtonHandle(ActionEvent actionEvent) {
        if(mazeDisplayerCanvas.getMaze() != null){
            mazeDisplayerCanvas.ClearAllCanvas();
        }
        TextFieldRow.clear();
        TextFieldCol.clear();
        SolveMazeButton.setDisable(true);
        ValidNumberLabel.setVisible(false);
        SaveMazeButton.setVisible(false);
        SolveMazeButton.setText("Solve Maze");
    }

    //########## About Handler ##########//
    public void AboutButtonHandle(){
        Alert AboutAlert = new Alert(Alert.AlertType.INFORMATION);
        AboutAlert.setTitle("About Us");
        Stage AboutStage = (Stage) AboutAlert.getDialogPane().getScene().getWindow();
        Image imageNameSol = null;
        try{
            imageNameSol = new Image(new FileInputStream("./src/Sources/Images/AboutUsIcon.jpg"));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        AboutStage.getIcons().add(imageNameSol);
        Parent Root = null;
        try {
            Root = FXMLLoader.load(getClass().getResource("AboutWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        AboutStage.setScene(new Scene(Root,800,500));
        AboutStage.show();
    }

    //########## Help Handler ##########//
    public void HelpButtonHandle(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Help Window");
        Stage helpStage = (Stage) a.getDialogPane().getScene().getWindow();
        Image IconImageHelp = null;
        try{
            IconImageHelp = new Image(new FileInputStream("./src/Sources/Images/HelpIcon.jpg"));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        helpStage.getIcons().add(IconImageHelp);
        Parent Root = null;
        try {
            Root = FXMLLoader.load(getClass().getResource("HelpWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        helpStage.setScene(new Scene(Root,700,460));
        helpStage.show();
    }

    //########## Exit Handler ##########//
    public void ExitButtonHandle() {
        viewModel.CloseServers();
    }

    public void PropertiesHandle() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        Stage propertiesStage = (Stage) a.getDialogPane().getScene().getWindow();
        Image IconImageProperties = null;
        try{
            IconImageProperties = new Image(new FileInputStream("./src/Sources/Images/SittingsIcon.png"));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        propertiesStage.getIcons().add(IconImageProperties);
        Parent Root = null;
        try {
            Root = FXMLLoader.load(getClass().getResource("PropertiesWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertiesStage.setScene(new Scene(Root,700,460));
        propertiesStage.show();
        NewButtonHandle(new ActionEvent());
    }

}
