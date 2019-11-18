package com.emailserver.beans;

import com.sun.tools.javac.util.List;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {

    private long id;
    private transient User sender;
    private User[] receivers;
    private Date dateSent;
    private String subject;
    private String text;

    public Email(User sender, User[] receivers, Date dateSent, String subject, String text) {
        this.sender = sender;
        this.receivers = receivers;
        this.dateSent = dateSent;
        this.subject = subject;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User[] getReceivers() {
        return receivers;
    }

    public void setReceivers(User[] receivers) {
        this.receivers = receivers;
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

    public String toString() {
        return "Mail: " + getSender().getAddress() + ", " + getText();
    }

}
