package fx;

import controllers.ComposeMailController;
import controllers.MainController;
import controllers.ShowMailController;
import io.ConfigReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import model.MainModel;
import shared.Email;
import java.net.URL;

public class MainApplication extends Application {

    public static final String MAIN_CONTEXT = "../main.fxml";
    public static final String RES_STYLESHEET_MAIN = "../styles.css";
    public static final String COMPOSE_CONTEXT = "../compose_email.fxml";
    public static final String SHOW_MAIL_CONTEXT = "../show_email.fxml";

    private MainModel model;
    private static StackPane contextSwitcher;

    // Contexts (save references in order to switch between them)
    private static BorderPane composePane;
    private static BorderPane mainPane;
    private static BorderPane showMailPane;

    private static MainController mainController;
    private static ComposeMailController composeController;
    private static ShowMailController showMailController;

    @Override
    public void start(Stage stage) throws Exception {
        ConfigReader.getIdentity();
        URL styleSheet = getClass().getResource(RES_STYLESHEET_MAIN);
        contextSwitcher = new StackPane();
        // Get the layout and resource files
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(MAIN_CONTEXT));
        FXMLLoader composeLoader = new FXMLLoader(getClass().getResource(COMPOSE_CONTEXT));
        FXMLLoader showMailLoader = new FXMLLoader(getClass().getResource(SHOW_MAIL_CONTEXT));
        // Load panes of the application
        showMailPane = showMailLoader.load();
        composePane = composeLoader.load();
        mainPane = mainLoader.load();
        // Add every Pane to the contextSwitcher children list
        contextSwitcher.getChildren().add(showMailPane);
        contextSwitcher.getChildren().add(mainPane);
        contextSwitcher.getChildren().add(composePane);
        // Create a new scene with contextSwitcher as main panel
        Scene mainScene = new Scene(contextSwitcher);
        mainScene.getStylesheets().add(styleSheet.toExternalForm());

        model = new MainModel();
        composeController = composeLoader.getController();
        mainController = mainLoader.getController();
        showMailController = showMailLoader.getController();

        composeController.bindModel(model);
        mainController.bindModel(model);

        stage.setTitle("Email client");
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
        composePane.setVisible(true);
        mainPane.setVisible(false);
        showMailPane.setVisible(false);
    }

    public static void switchToMain() {
        mainPane.setVisible(true);
        composePane.setVisible(false);
        showMailPane.setVisible(false);
    }

    public static void switchToShowMail(Email toShow) {
        if(toShow != null) {
            showMailController.show(toShow);
            mainPane.setVisible(false);
            composePane.setVisible(false);
            showMailPane.setVisible(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
