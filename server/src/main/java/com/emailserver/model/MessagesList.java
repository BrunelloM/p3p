package com.emailserver.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * This is the model class: It hold every message that the threads will write
 */
public class MessagesList {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");

    private ObservableList<String> messagesHolder;
    private boolean binded = false;

    public MessagesList() {
        messagesHolder = FXCollections.observableArrayList();
    }

    public synchronized void addMessage(String message) {
        String timestampedMessage = "[" + sdf.format(new Timestamp(System.currentTimeMillis())) + "]  " + message;
        Platform.runLater(() -> messagesHolder.add(timestampedMessage));
    }

    public void bindListView(ListView<String> toBind) {
        if(!binded) {
            toBind.setItems(messagesHolder);
            binded = true;
        }
    }

}
