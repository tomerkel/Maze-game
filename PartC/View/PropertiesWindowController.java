package View;

import Server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * this class will be the controller to properties window
 */
public class PropertiesWindowController {
    @FXML
    public ComboBox MazeGeneratorBox;
    @FXML
    public ComboBox MazeSolutionBox;

    public void PropertiesButtonHandel(ActionEvent actionEvent) {
        String GeneratorName = (String) MazeGeneratorBox.getValue();
        String SolutionName = (String) MazeSolutionBox.getValue();
        Server.setConfigurations("GenerateAlgorithm", GeneratorName);
        Server.setConfigurations("SearchAlgorithm", SolutionName);

        ((Stage)MazeGeneratorBox.getScene().getWindow()).close();
    }
}
