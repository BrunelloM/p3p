package com.emailserver.ui;

import com.emailserver.model.MessagesList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class.
 * It initializes each application component
 *
 * @author Matteo Brunello
 */
public class MainApplication extends Application {

    private static final String FXML_SCENE_PATH = "/emailserver/main.fxml";
    private static final String STYLESHEET_PATH = "/emailserver/style.css";

    @Override
    public void start(Stage stage) throws Exception {
        // Creates the model (By creating it here it will only have one model shared across multiple controllers in case they will be added)
        MessagesList model = new MessagesList();
        MainController controller = new MainController(model);  // Creates the controller and binds to it the previously created model

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_SCENE_PATH));
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_PATH).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
