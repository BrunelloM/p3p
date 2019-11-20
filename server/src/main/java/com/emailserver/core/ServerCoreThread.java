package com.emailserver.core;

import com.emailserver.io.FilesManager;
import com.emailserver.model.Messages;
import com.emailserver.model.MessagesList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ServerCoreThread extends Thread {

    private final int serverPort = 1055;
    private ServerSocket socket;
    private ExecutorService requestsPool;
    private MessagesList model;
    private boolean hasErrors = false;

    public ServerCoreThread(MessagesList model) {
        this.setDaemon(true);
        this.model = model;
        requestsPool = ExecutorManager.getExecutorService();
        try {
            socket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            coreThreadError(e);
        }
    }

    @Override
    public void run() {
        model.addMessage(Messages.INIT_DIRECTORIES);
        FilesManager.init();
        model.addMessage(Messages.INIT_USERS_TABLE);
        UsersTable.init();
        model.addMessage(String.format(Messages.SERVER_STARTED, serverPort));
        try {
            while(!Thread.interrupted()) {
                Socket incoming = socket.accept();
                requestsPool.execute(new RequestHandler(incoming));
            }
        } catch (IOException e) {
            e.printStackTrace();
            coreThreadError(e);
        }
    }

    @Override
    public void interrupt() {
        try{
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.interrupt();
    }

    public boolean canStart() {
        return !hasErrors;
    }

    private void coreThreadError(Exception e) {
        model.addMessage("Fatal Error: " + e.getMessage());
        hasErrors = true;
        this.interrupt();
    }

}
