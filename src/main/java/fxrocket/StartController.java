package fxrocket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;


public class StartController {
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Platform.exit();
    }
}
