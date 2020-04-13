package controllers;

import fx.MainApplication;
import io.ConfigReader;
import javafx.scene.control.Button;
import model.MainModel;
import fx.EmailListCell;
import javafx.scene.control.Label;
import shared.Email;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import shared.User;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends BindableController implements Initializable {

    enum State {
        INBOX,
        SENT,
        SPECIAL,
        TRASH
    }

    @FXML private Label identityNameSurnameHolder;
    @FXML private Label identityEmailHolder;

    // Needs their reference in order to change their visibilities.
    @FXML private Button starButton;
    @FXML private Button replyButton;

    @FXML private Label topBarTitle;
    @FXML private ImageView avatarImageHolder;
    @FXML private ListView<Email> emailListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        identityEmailHolder.setText(ConfigReader.getIdentity().getAddress());
        String nameSurname = ConfigReader.getIdentity().getName() + " " + ConfigReader.getIdentity().getSurname();
        identityNameSurnameHolder.setText(nameSurname.toUpperCase());
        // Setup Circle avatar
        Circle circle = new Circle(64);
        circle.setCenterX(64);
        circle.setCenterY(64);
        avatarImageHolder.setClip(circle);

        // Email list view init
        emailListView.setCellFactory(new EmailListCell.EmailListCellFactory());
        emailListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                MainApplication.switchToShowMail(emailListView.getSelectionModel().getSelectedItem()); // Show the email when the email gets double clicked
            }
        });

    }

    /**
     * Called upon deleteButton click event
     */
    @FXML
    private void deleteButtonClick() {
        Email selected = emailListView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            model.delete(selected);
        }
    }

    /**
     * Called upon replyButton click event
     */
    @FXML
    private void replyButtonClick() {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        if(email != null) {
            MainApplication.switchToCompose(email);
        }
    }

    /**
     * Called upon composeButton click event
     */
    @FXML
    private void composeButtonClick() {
        MainApplication.switchToCompose();
    }

    /**
     * Called upon starButton click event
     */
    @FXML
    private void starButtonClick() {
        Email email = emailListView.getSelectionModel().getSelectedItem();
        if(email != null) {
            model.star(email);
        }
    }

    /** +========+ SIDE NAVIGATION BAR BUTTONS HANDLERS +========+ **/
    /**
     * Called upon side navigation buttons click events
     */
    @FXML
    private void inboxButtonClick() {
        setState(State.INBOX);
    }

    @FXML
    private void sentButtonClick() {
        setState(State.SENT);
    }

    @FXML
    private void specialButtonClick() {
        setState(State.SPECIAL);
    }

    @FXML
    private void trashButtonClick() {
        setState(State.TRASH);
    }

    @Override
    public void bindModel(MainModel model) {
        super.bindModel(model);
        model.bind(emailListView);              // Bind the ListView widget to the model
    }


    /**
     * Changes the UI application state.
     * State is an enum structure.
     * @param state The state that will be represented
     */
    private void setState(State state) {
        switch (state) {
            case INBOX:
                model.filter(Email.Type.RECEIVED);
                replyButton.setVisible(true);
                starButton.setVisible(true);
                topBarTitle.setText("Inbox");
            break;

            case SENT:
                model.filter(Email.Type.SENT);
                replyButton.setVisible(false);
                starButton.setVisible(false);
                topBarTitle.setText("Sent");
            break;

            case SPECIAL:
                model.filter(Email.Type.SPECIAL);
                replyButton.setVisible(true);
                starButton.setVisible(false);
                topBarTitle.setText("Special");
            break;

            case TRASH:
                replyButton.setVisible(false);
                starButton.setVisible(false);
                model.filter(Email.Type.TRASH);
                topBarTitle.setText("Trash");
            break;

            default:
                throw new IllegalArgumentException();
        }
    }

}

