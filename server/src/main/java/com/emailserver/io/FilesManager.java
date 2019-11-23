package com.emailserver.io;

import shared.Email;
import shared.User;
import com.emailserver.core.UsersTable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesManager {

    private static final char SEPARATOR = File.separatorChar;
    private static final String DATA_FOLDER = "." + SEPARATOR + "data" + SEPARATOR;
    private static final String SPECIAL_FOLDER = "special";
    private static final String SENT_FOLDER = "sent";
    private static final String TRASH_FOLDER = "trash";
    private static final String RECEIVED_FOLDER = "received";
    private static final String FILE_EXTENSION = ".ser";
    private static final String USERS_FILE = "users.txt";
    private static final String PARAM_PATH = DATA_FOLDER + "%d" + SEPARATOR + "%s" + SEPARATOR + "%d" + FILE_EXTENSION;

    /**
     * Creates for each user in the "user.txt" file a folder (named with the id of the user) and
     * their corresponding inbox, sent, trash and special folders
     */
    public static void init() {
        for(User user : Objects.requireNonNull(getUsers())) {
            createFolders(DATA_FOLDER + user.getId() + SEPARATOR + SENT_FOLDER);        // ./data/id/sent
            createFolders(DATA_FOLDER + user.getId() + SEPARATOR + RECEIVED_FOLDER);    // ./data/id/received
            createFolders(DATA_FOLDER + user.getId() + SEPARATOR + TRASH_FOLDER);       // ./data/id/trash
            createFolders(DATA_FOLDER + user.getId() + SEPARATOR + SPECIAL_FOLDER);     // ./data/id/special
        }
    }

    /**
     * TODO Add javadoc here
     * @param toWrite
     */
    public static synchronized void saveEmail(Email toWrite) {
        try {
            toWrite.setupDateAndId();
            String filePath = String.format(PARAM_PATH, UsersTable.get(toWrite.getSender()).get().getId(), SENT_FOLDER, toWrite.getId());
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(toWrite);
            oos.close();
            fos.close();
            // Save the email in the corresponding inbox folder of each receiver
            for(String recAddress : toWrite.getRecipients()) {
                filePath = String.format(PARAM_PATH, UsersTable.get(recAddress).get().getId(), RECEIVED_FOLDER, toWrite.getId());
                fos = new FileOutputStream(filePath);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(toWrite);
                oos.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param user
     * @return
     */
    public static synchronized List<Email> getSent(User user) {
        return readEmails(user, SENT_FOLDER);
    }

    /**
     * @param user
     * @return
     */
    public static synchronized List<Email> getTrash(User user) {
        return readEmails(user, TRASH_FOLDER);
    }

    /**
     * @param user
     * @return
     */
    public static synchronized List<Email> getInbox(User user) {
        return readEmails(user, RECEIVED_FOLDER);
    }

    /**
     * @param user
     * @return
     */
    public static synchronized List<Email> getSpecials(User user) {
        return readEmails(user, SPECIAL_FOLDER);
    }

    /**
     * @param user
     * @param folderName
     * @return
     */
    private static List<Email> readEmails(User user, String folderName) {
        File[] directory = new File(DATA_FOLDER + user.getId() + SEPARATOR + folderName).listFiles();
        if(directory != null) {
            return Stream.of(directory)
                    .map(FilesManager::readEmail)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Read a single email file, it takes the file as input and returns the email object that has been deserialized.
     * If it fails it throws a fatal exception.
     * @param file The file that contains the serialized mail object
     * @return The email that has been read and deserialized from the given file
     */
    private static Email readEmail(File file) {
        try(ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (Email) objInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user
     * @param email
     */
    public static synchronized void trashInboxEmail(User user, Email email) {
        moveFile(
                String.format(PARAM_PATH, user.getId(), RECEIVED_FOLDER, email.getId()),
                String.format(PARAM_PATH, user.getId(), TRASH_FOLDER, email.getId())
        );
    }

    /**
     * @param user
     * @param email
     */
    public static synchronized void trashSentEmail(User user, Email email) {
        moveFile(
                String.format(PARAM_PATH, user.getId(), SENT_FOLDER, email.getId()),
                String.format(PARAM_PATH, user.getId(), TRASH_FOLDER, email.getId())
        );
    }

    /**
     * @param user
     * @param email
     */
    public static synchronized void trashSpecialEmail(User user, Email email) {
        moveFile(
                String.format(PARAM_PATH, user.getId(), SPECIAL_FOLDER, email.getId()),
                String.format(PARAM_PATH, user.getId(), TRASH_FOLDER, email.getId())
        );
    }

    /**
     * @param user
     * @param email
     */
    public static synchronized void starInboxEmail(User user, Email email) {
        moveFile(
                String.format(PARAM_PATH, user.getId(), RECEIVED_FOLDER, email.getId()),
                String.format(PARAM_PATH, user.getId(), SPECIAL_FOLDER, email.getId())
        );
    }

    /**
     * @param path
     * @param newPath
     */
    private static synchronized void moveFile(String path, String newPath) {
        Path temp = null;
        try {
            temp = Files.move(Paths.get(path), Paths.get(newPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the folders if they doesn't exists given a particular path
     * @param path String that represents the path with the folders to be created
     */
    private static void createFolders(String path) {
        File file = new File(path);
        try {
            if(!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a list of Users from the USERS file: It uses Streams (They are lazy functional lists) in order to
     * use operations like map, filter ecc..
     * @return A list of users on the USERS_FILE
     */
    public static List<User> getUsers() {
        try {
            Stream<String> userStream = Files.lines(Paths.get(DATA_FOLDER + USERS_FILE));
            return userStream
                    .map(s -> s.split("\\s*;\\s*"))
                    .map(a -> new User(Integer.parseInt(a[0]), a[1], a[2], a[3]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);      // In this case we have a fatal exception
        }
    }

}
