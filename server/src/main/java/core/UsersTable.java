package core;

import io.FilesManager;
import io.ServerStorage;
import shared.User;
import java.util.LinkedHashMap;

public class UsersTable {

    private static LinkedHashMap<String, User> addressUserTable;

    /**
     * +=== !!!!!!! ===+ IMPORTANT +=== !!!!!!! ===+
     * + This function should be called whenever   +
     * + the application starts.                   +
     * +===========================================+
     *
     * Initializes the UserTable with keys and values. It reads values using FilesManager from the users.txt file
     */
    public static void init() {
        ServerStorage storage = new FilesManager();
        if(addressUserTable == null) {
            addressUserTable = new LinkedHashMap<>();
            for(User user : storage.getUsers()) {
                addressUserTable.put(user.getAddress(), user.clone());
            }
        }
    }

    /**
     * Since the get function could return a non valid value (null) we wrap it in an Optional type.
     * @param address The email address to search
     * @return An empty Optional if LinkedHashMap.get() returns null, otherwise an optional containing the user which corresponds to that email address
     */
    public static User get(String address) {
        return addressUserTable.get(address);
    }

    /**
     * Check if the table contains a given email address (Which corresponds to a user)
     * @param address The email address to search
     * @return True if the hashmap contains the email address, false otherwise
     */
    public static boolean contains(String address) {
        return addressUserTable.containsKey(address);
    }


    /**
     * Check if the table has been initialized
     * @return True if the table has been initialized, false otherwise
     */
    public static boolean isInitialized() {
        return addressUserTable != null;
    }
}
