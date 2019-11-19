package com.emailserver;

import com.emailserver.core.ServerCoreThread;
import com.emailserver.core.UsersTable;
import com.emailserver.io.FilesManager;

public class ApplicationStarter {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        FilesManager.init();
        UsersTable.init();       // Initialize userList

        Thread t = new Thread(new ServerCoreThread(10956));
        t.start();
    }

}
