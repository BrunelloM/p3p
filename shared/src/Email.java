package shared;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

    private long id;
    private String sender;
    private String[] recipients;
    private Date dateSent;
    private String subject;
    private String text;

    public Email(long id, String sender, String[] recipients, Date dateSent, String subject, String text) {
        this.id = id;
        this.sender = sender;
        this.recipients = recipients;
        this.dateSent = dateSent;
        this.subject = subject;
        this.text = text;
    }

    public Email(String sender, String[] recipients, String subject, String text) {
        this.dateSent = new Date();
        this.id = dateSent.getTime();
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.text = text;
    }

    public void setupDateAndId() {
        if(this.dateSent == null) {
            this.dateSent = new Date();
            this.id = dateSent.getTime();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Email clone() {
        return new Email(id, sender, recipients, dateSent, subject, text);
    }

}
