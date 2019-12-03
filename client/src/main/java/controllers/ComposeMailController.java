package controllers;

import fx.MainApplication;
import io.ConfigReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import shared.Email;
import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMailController extends BindableController implements Initializable {

    @FXML private TextField recipientsTxt;
    @FXML private TextField subjectTxt;
    @FXML private TextArea textTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: Nothing to initialize?
    }

    private boolean checkFields() {
        //TODO: To finish
        return true;
    }

    public void replyState(Email email) {
        StringBuilder builder = new StringBuilder();
        String[] recipients = email.getRecipients();
        for(int i = 0; i < recipients.length - 1; i++) {
            builder.append(recipients[i]);
            builder.append(", ");
        }
        builder.append(recipients[recipients.length - 1]); // Append the last mail address recipient
        recipientsTxt.setText(builder.toString());
        subjectTxt.setText("RE: " + email.getSubject());
    }

    @FXML
    private void onBackButtonClick() {
        MainApplication.switchToMain();
    }

    @FXML
    private void onSendButtonClick() {
        if(checkFields()) {
            String[] recipients = recipientsTxt.getText().split(", ");
            String subject = subjectTxt.getText();
            String text = textTxt.getText();
            Email email  = new Email(ConfigReader.getIdentity().getAddress(), recipients, subject, text);
            model.send(email);
        }
    }
}
