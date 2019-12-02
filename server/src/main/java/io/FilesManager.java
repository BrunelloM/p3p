package io;

import core.UsersTable;
import shared.Email;
import shared.User;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesManager implements ServerStorage {

    private static final char SEPARATOR = File.separatorChar;
    private static final String DATA_FOLDER = "." + SEPARATOR + "data" + SEPARATOR;
    private static final String FILE_EXTENSION = ".ser";
    private static final String USERS_FILE = "users.dat";
    private static final String PARAM_PATH = DATA_FOLDER + "%d" + SEPARATOR + "%d" + FILE_EXTENSION;

    /**
     * Creates every User folders in order to work.
     */
    public FilesManager() {
        for(User user : getUsers())
            createFolders(DATA_FOLDER + user.getId());
    }

    /**
     * Modifies a given email type with a new give type
     * It reads the Email, then it changes its type and lastly it overrides the old Email serialized file.
     * @param user User where the mail belongs
     * @param toModify Email on which the type has to be modified
     * @param newType The new modified type of the Email
     */
    @Override
    public synchronized void setEmailType(User user, Email toModify, Email.Type newType) {
        try {
            String filePath = String.format(PARAM_PATH, user.getId(), toModify.getId());
            Email modified = readEmail(new File(filePath));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
            modified.setType(newType);
            oos.writeObject(modified);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEmail(User user, Email toDelete) {
        String filePath = String.format(PARAM_PATH, user.getId(), toDelete.getId());
        File fileToDelete = new File(filePath);
        fileToDelete.delete();
    }

    /**
     * Saves a given Email.
     * It writes in the sender folder the email with SENT type, and then for each recipient
     * it saves the same mail with RECEIVED type.
     * @param toWrite Email object to save
     */
    @Override
    public synchronized void saveEmail(Email toWrite) {
        try {
            toWrite.setupDateAndId();
            String filePath = String.format(PARAM_PATH, UsersTable.get(toWrite.getSender()).getId(), toWrite.getId());
            toWrite.setType(Email.Type.SENT);
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(toWrite);
            oos.close();
            fos.close();
            // Save the email in the corresponding inbox folder of each receiver
            toWrite.setType(Email.Type.RECEIVED);
            for(String recAddress : toWrite.getRecipients()) {
                filePath = String.format(PARAM_PATH, UsersTable.get(recAddress).getId(), toWrite.getId());
                fos = new FileOutputStream(filePath);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(toWrite);
                oos.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user User on which you want to read the email
     * @return  A list of Emails belonging to the given user
     */
    @Override
    public synchronized List<Email> readEmails(User user) {
        File[] directory = new File(DATA_FOLDER + user.getId()).listFiles();
        if(directory != null) {
            return Stream.of(directory)
                    .map(this::readEmail)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Get a list of Users from the USERS file: It uses Streams (They are lazy functional lists) in order to
     * use operations like map, filter ecc..
     * @return A list of users on the USERS_FILE
     */
    @Override
    public List<User> getUsers() {
        try {
            Stream<String> userStream = Files.lines(Paths.get(DATA_FOLDER + USERS_FILE));
            return userStream
                    .map(s -> s.split("\\s*;\\s*"))
                    .map(a -> new User(Integer.parseInt(a[0]), a[1], a[2], a[3]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Read a single email file, it takes the file as input and returns the email object that has been deserialized.
     * If it fails it throws a fatal exception.
     * @param file The file that contains the serialized mail object
     * @return The email that has been read and deserialized from the given file
     */
    private Email readEmail(File file) {
        try(ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (Email) objInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the folders if they doesn't exists given a particular path
     * @param path String that represents the path with the folders to be created
     */
    private void createFolders(String path) {
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

}
