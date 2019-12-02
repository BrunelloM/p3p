package fx;

import core.ServerDaemon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Messages;
import model.MessagesModel;
import java.io.IOException;
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
    private MessagesModel model;                     // Model of the MVC pattern (Message List) )
    private ServerDaemon coreThread;                 // Server core thread, it will handle every client requests

    MainController(MessagesModel model) {
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
        if(this.coreThread == null) {
            try {
                this.coreThread = new ServerDaemon(model);
                coreThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                model.addMessage(String.format(Messages.ERROR_MSG, e.getMessage()));    // Prints the error message
            }
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