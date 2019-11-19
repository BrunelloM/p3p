package com.emailserver;

import com.emailserver.core.ServerCoreThread;
import com.emailserver.core.UsersList;
import com.emailserver.io.FilesManager;
import sun.jvm.hotspot.memory.FileMapInfo;

public class ApplicationStarter {

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        FilesManager.init();
        UsersList.init();       // Initialize userList

        Thread t = new Thread(new ServerCoreThread(10956));
        t.start();
    }

}
