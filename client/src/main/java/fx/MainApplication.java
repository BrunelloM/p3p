package fx;

import controllers.BindableController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import model.MainModel;

public class MainApplication extends Application {

    private static final String RES_LAYOUT_MAIN = "../main.fxml";
    private static final String RES_STYLESHEET_MAIN = "../styles.css";

    private MainModel model;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainRoot = new FXMLLoader(getClass().getResource(RES_LAYOUT_MAIN));
        Scene mainScene = new Scene(mainRoot.load());
        mainScene.getStylesheets().add(getClass().getResource(RES_STYLESHEET_MAIN).toExternalForm());

        model = new MainModel();

        BindableController mainController = mainRoot.getController();
        mainController.bindModel(model);

        stage.setScene(mainScene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        model.tearDown();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
