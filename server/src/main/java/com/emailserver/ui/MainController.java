package com.emailserver.ui;

import com.emailserver.core.ServerCoreThread;
import com.emailserver.model.MessagesList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ListView<String> messagesList;
    private MessagesList model;
    private ServerCoreThread coreThread;

    MainController(MessagesList model) {
        this.model = model;
    }

    /**
     * This method initializes the Server: It checks if the server has thrown
     * an error, start the server if it has been created successfully.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.bindListView(messagesList);
    }

    @FXML
    void onStartClick() {
        if(coreThread == null) {
            coreThread = new ServerCoreThread(model);
            if(coreThread.canStart())
                coreThread.start();
        }
    }

    @FXML
    void onStopClick() {
        coreThread.interrupt();
        coreThread = null;
    }

}
