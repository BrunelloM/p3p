package com.emailserver.core;

import com.emailserver.beans.User;
import com.emailserver.io.FilesManager;
import java.util.List;
import java.util.ArrayList;

public class UsersList {

    private static List<User> userList = null;

    public static void init() {
        if(userList == null) {
            userList = new ArrayList<>();
            userList.addAll(FilesManager.getUsers());
        }
    }

    public static List<User> getList() {
        return new ArrayList<>(userList);
    }

    public static boolean contains(User user) {
        return userList.contains(user);
    }

}
