package com.emailserver.ui;

import com.emailserver.core.ServerCoreThread;
import com.emailserver.model.MessagesList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *  Main controller class.
 *  It handles users clicks and it holds the CoreServerThread reference in order to stop/create a new one upon user request
 *
 *  @author Matteo Brunello
 */
public class MainController implements Initializable {

    @FXML
    private ListView<String> messagesList;
    private MessagesList model;                     // Model of the MVC pattern (Message List) )
    private ServerCoreThread coreThread;            // Server core thread, it will handle every client requests

    MainController(MessagesList model) {
        this.model = model;
    }

    /**
     * In this method we just bin the model to the ListView
     * In this case we bin the observable list of messages
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.bindListView(messagesList);
    }

    /**
     * Method that gets fired when the user clicks the start button on the GUI.
     * It creates the CoreServerThread (only if it hasn't been already created) that will handle clients requests.
     */
    @FXML
    void onStartClick() {
        if(coreThread == null) {
            coreThread = new ServerCoreThread(model);
            if(coreThread.canStart())
                coreThread.start();
        }
    }

    /**
     * Method that gets fired when the user clicks the stop button on the GUI.
     * It stops the CoreServerThread only if it has been already created.
     */
    @FXML
    void onStopClick() {
        if(coreThread != null) {
            coreThread.interrupt();
            coreThread = null;
        }
    }

}
