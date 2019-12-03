package controllers;

import fx.MainApplication;
import model.MainModel;
import fx.EmailListCell;
import javafx.scene.control.Label;
import shared.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BindableController implements Initializable {

    private static final String RES_COMPOSE_LAYOUT = "../compose_email.fxml";

    private boolean isRunning = false;
    @FXML private Label topBarTitle;
    @FXML private ImageView avatarImageHolder;
    @FXML private ListView<Email> emailListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup Circle avatar
        Circle circle = new Circle(64);
        circle.setCenterX(64);
        circle.setCenterY(64);
        avatarImageHolder.setClip(circle);

        // Email list view init
        emailListView.setCellFactory(new EmailListCell.EmailListCellFactory());
    }

    @FXML
    private void deleteButtonClick() {
        Email selected = emailListView.getSelectionModel().getSelectedItem();
        model.delete(selected);
    }

    @FXML
    private void replyButtonClick(ActionEvent event) throws IOException {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        MainApplication.switchToCompose(email);
    }

    @FXML
    private void composeButtonClick(ActionEvent event) {
        MainApplication.switchToCompose();
    }

    @FXML
    private void replyAllButtonClick() {

    }

    /** +========+ SIDE NAVIGATION BAR BUTTONS HANDLERS +========+ **/
    @FXML
    private void inboxButtonClick() {
        model.filter(Email.Type.RECEIVED);
        topBarTitle.setText("Inbox");
    }

    @FXML
    private void sentButtonClick() {
        model.filter(Email.Type.SENT);
        topBarTitle.setText("Sent");
    }

    @FXML
    private void specialButtonClick() {
        model.filter(Email.Type.SPECIAL);
        topBarTitle.setText("Specials");
    }

    @FXML
    private void trashButtonClick() {
        model.filter(Email.Type.TRASH);
        topBarTitle.setText("Trash");
    }

    @Override
    public void bindModel(MainModel model) {
        super.bindModel(model);
        model.bind(emailListView);              // Bind the ListView widget to the model
    }

}

