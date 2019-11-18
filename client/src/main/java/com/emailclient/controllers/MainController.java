package com.emailclient.controllers;

import com.emailclient.beans.Email;
import com.emailclient.ui.EmailListCell;
import com.emailclient.ui.SideBarButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private ImageView avatarImageHolder;
    @FXML private ListView<Email> emailListView;
    @FXML private ToggleGroup sideNavButtons;
    @FXML private SideBarButton inboxButton;

    private ObservableList<Email> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Circle avatar
        Circle circle = new Circle(64);
        circle.setCenterX(64);
        circle.setCenterY(64);
        avatarImageHolder.setClip(circle);

        // Email list view init
        emailListView.setCellFactory(new EmailListCell.EmailListCellFactory());
        data = FXCollections.observableArrayList(
                new Email("Gesu' di Nazareth", "Consegna croce", "12:32 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Edoardo Chiavazza", "Richiesta codifica", "2:01 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Nome e cognome", "Consectetur adipiscing elit", "18:30 P.M","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Lorem ipsum dolor", "Consectetur adipiscing elit", "Yesterday","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Lorem ipsum dolor", "Consectetur adipiscing elit", "12:32 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Edoardo Chiavazza", "Consectetur adipiscing elit", "12:32 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Nome e cognome", "Consectetur adipiscing elit", "12:32 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Lorem ipsum dolor", "Consectetur adipiscing elit", "12:32 A.M.","Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et."),
                new Email("Lorem ipsum dolor", "Consectetur adipiscing elit","12:32 A.M.", "Mauris ullamcorper vel augue et imperdiet. Phasellus et dolor ligula. Aenean semper scelerisque auctor. Nam iaculis tellus elit, at congue mauris venenatis quis. Nam eleifend eros a tempus pellentesque. Vestibulum ligula dui, suscipit id eleifend at, pellentesque eu magna. Maecenas vehicula sagittis augue a tristique. Nam eleifend sapien bibendum lacus tristique accumsan. Vestibulum aliquam rhoncus dui, at faucibus libero sollicitudin et.")
        );

        emailListView.setItems(data);
        inboxButton.setSelected(true);
    }

    @FXML
    private void deleteButtonClick() {
        data.remove(emailListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void replyButtonClick() {
        // TODO: Compete this method
    }

}

