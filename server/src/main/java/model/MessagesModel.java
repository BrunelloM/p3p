package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Model class of the MVC pattern.
 * In this case the UI will just consists of a list of messages.
 * The list of messages is an observable list, since the application's GUI is based on the JavaFX framework.
 */
public class MessagesModel {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
    private ObservableList<String> messagesList;
    private ListView<String> listView;

    public MessagesModel() {
        this.messagesList = FXCollections.observableArrayList();
        this.messagesList.addListener(
            (ListChangeListener.Change<? extends String> change) -> {
                listView.scrollTo(messagesList.size() - 1);
            }
        );
    }

    /**
     * Adds a specific message to the model by changing its state.
     * The UI will be automatically updated if binded to this model.
     * @param message The String message to be added
     */
    public void addMessage(String message) {
        String timestampedMessage = "[" + sdf.format(new Timestamp(System.currentTimeMillis())) + "]  " + message;
        Platform.runLater(() -> this.messagesList.add(timestampedMessage));
    }

    /**
     * Binds this model to a given ListView
     * @param listView ListView on which the model will be binded
     */
    public void bindListView(ListView<String> listView) {
        if(listView != null) {
            listView.setItems(messagesList);
            this.listView = listView;
        }
    }

}
