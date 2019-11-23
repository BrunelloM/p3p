package com.emailserver.core;

import shared.User;
import com.emailserver.io.FilesManager;
import java.util.LinkedHashMap;
import java.util.Optional;

public class UsersTable {

    private static LinkedHashMap<String, User> addressUserTable;

    /**
     * +=== !!!!!!! ===+ IMPORTANT +=== !!!!!!! ===+
     * + This function should be called whenever   +
     * + the application starts.                   +
     * +===========================================+
     *
     * Initializes the UserTable with keys and values. It reads values using core.io.FilesManager from the users.txt file
     */
    public static void init() {
        if(addressUserTable == null) {
            addressUserTable = new LinkedHashMap<>();
            for(User user : FilesManager.getUsers()) {
                addressUserTable.put(user.getAddress(), user.clone());
            }
        }
    }

    /**
     * Since the get function could return a non valid value (null) we wrap it in an Optional type.
     * @param address The email address to search
     * @return An empty Optional if LinkedHashMap.get() returns null, otherwise an optional containing the user which corresponds to that email address
     */
    public static Optional<User> get(String address) {
        User user = addressUserTable.get(address);
        return Optional.ofNullable(user);
    }

    /**
     * Check if the table contains a given email address (Which corresponds to a user)
     * @param address The email address to search
     * @return True if the hashmap contains the email address, false otherwise
     */
    public static boolean contains(String address) {
        return addressUserTable.containsKey(address);
    }

}
