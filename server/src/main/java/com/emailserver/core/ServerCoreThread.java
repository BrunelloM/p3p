package com.emailserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ServerCoreThread implements Runnable {

    private ServerSocket socket;
    private ExecutorService requestsPool;

    public ServerCoreThread(int port) {
        try {
            requestsPool = ExecutorManager.getExecutorService();
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Core Thread started: Waiting for incoming connection..");
        try {
            while(true) {
                Socket incoming = socket.accept();
                requestsPool.execute(new RequestHandler(incoming));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
