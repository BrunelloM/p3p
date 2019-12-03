package fx;

import controllers.BindableController;
import controllers.ComposeMailController;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import model.MainModel;
import shared.Email;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {

    public static final String MAIN_CONTEXT = "../main.fxml";
    public static final String RES_STYLESHEET_MAIN = "../styles.css";
    public static final String COMPOSE_CONTEXT = "../compose_email.fxml";

    private MainModel model;
    private static StackPane contextSwitcher;

    // Contexts (save references in order to switch between them)
    private static BorderPane composePane;
    private static BorderPane mainPane;

    private static MainController mainController;
    private static ComposeMailController composeController;

    @Override
    public void start(Stage stage) throws Exception {
        URL styleSheet = getClass().getResource(RES_STYLESHEET_MAIN);
        contextSwitcher = new StackPane();
        // Get the layout and resource files
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(MAIN_CONTEXT));
        FXMLLoader composeLoader = new FXMLLoader(getClass().getResource(COMPOSE_CONTEXT));
        // Load panes of the application
        composePane = composeLoader.load();
        mainPane = mainLoader.load();
        // Set default look of the application (compose is not shown)
        composePane.setVisible(false);
        // Add every Pane to the contextSwitcher children list
        contextSwitcher.getChildren().add(mainPane);
        contextSwitcher.getChildren().add(composePane);
        // Create a new scene with contextSwitcher as main panel
        Scene mainScene = new Scene(contextSwitcher);
        mainScene.getStylesheets().add(styleSheet.toExternalForm());

        model = new MainModel();
        composeController = composeLoader.getController();
        mainController = mainLoader.getController();

        composeController.bindModel(model);
        mainController.bindModel(model);

        stage.setScene(mainScene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        model.tearDown();
        super.stop();
    }

    public static void switchToCompose(Email email) {
        composeController.replyState(email);
        switchToCompose();
    }

    public static void switchToCompose() {
        mainPane.setVisible(false);
        composePane.setVisible(true);
    }

    public static void switchToMain() {
        mainPane.setVisible(true);
        composePane.setVisible(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
