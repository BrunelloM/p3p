package io;


import java.util.List;

import shared.Email;
import shared.User;

/**
 * This interface represents a generic ServerStorage.
 * It has been created in order to abstract away the actual implementation of the server's storage system.
 * This enables Unit tests to be run without creating any files or occurrence in the persistence layer of the application.
 */
public interface ServerStorage {
    List<User> getUsers();                                                   // Get the list of users
    List<Email> readEmails(User user);                                       // Get the whole Emails given a user
    void saveEmail(Email toSave);                                            // Save a specific Email
    void setEmailType(User user, Email toModify, Email.Type newType);        // Change a given Email type of an email that belongs to user
    void deleteEmail(User user, Email toDelete);
}
