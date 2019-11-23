package com.emailserver.core;

import com.emailserver.io.FilesManager;
import com.emailserver.model.Messages;
import com.emailserver.model.MessagesList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerDaemon extends Thread {

    private final int serverPort = 1055;

    private ServerSocket socket;                    // Connection of the server
    private ExecutorService requestsPool;           // Thread pool of requests
    private MessagesList model;                     // Model od MVC pattern
    private boolean hasErrors = false;              // flag that says if it has errors

    /**
     * Constructor of the ServerCoreThread.
     * It will set specific parameters and creates/get the ThreadPool needed for requests handling.
     * It also try to create a new ServerSocket on the specific serverPort.
     * If something goes wrong, it will set the hasErrors variable to true.
     *
     * @param model The model of MVC pattern (in this case a MessageList).
     */
    public ServerDaemon(MessagesList model) {
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

    /**
     * This method will be executed on a different Thread.
     * First it will initialize server's critical components such as data directories and users table that are required from it to function.
     * Then it will enter on a never-ending cycle (until thread is interrupted) that will accept new connections
     * and handle the incoming connection on another thread.
     * Those requests will be handled on a ThreadPool called requestsPool.
     */
    @Override
    public void run() {
        model.addMessage(Messages.INIT_DIRECTORIES);
        FilesManager.init();
        model.addMessage(Messages.INIT_USERS_TABLE);
        UsersTable.init();
        model.addMessage(String.format(Messages.SERVER_STARTED, serverPort));   // Server started and listening on port message
        try {
            while(!Thread.interrupted()) {
                Socket incoming = socket.accept();
                model.addMessage("New connection from " + incoming.getInetAddress() + " accepted");
                requestsPool.execute(new RequestThread(incoming, model));
            }
        } catch (IOException e) {
            if(!(e instanceof SocketException)) {
                e.printStackTrace();
                coreThreadError(e);
            }
        }
    }

    /**
     *
     */
    @Override
    public void interrupt() {
        try{
            model.addMessage(Messages.SERVER_STOPPING);
            requestsPool.shutdown();
            requestsPool.awaitTermination(5, TimeUnit.SECONDS);     // Wait for all requests to be completed (uses a timeout of 5 seconds)
            socket.close();                                           // Closes the server connection.
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        super.interrupt();
        model.addMessage(Messages.SERVER_STOPPED);
    }

    public boolean canStart() {
        return !hasErrors;
    }

    private void coreThreadError(Exception e) {
        model.addMessage(String.format(Messages.ERROR_MSG, e.getMessage()));
        hasErrors = true;
        this.interrupt();
    }

}
