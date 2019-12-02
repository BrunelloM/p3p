package fx;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertManager {

    /**
     * Creates and show and error Dialog in the JavaFX UI thread.
     * @param message Dialog body message
     */
    public static void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

}
