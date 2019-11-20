package com.emailserver.ui;

import com.emailserver.model.MessagesList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MessagesList model = new MessagesList();
        MainController controller = new MainController(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/emailserver/main.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/emailserver/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
