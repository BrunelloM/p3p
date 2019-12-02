package core;


import model.Messages;
import io.ConfigReader;
import io.FilesManager;
import java.net.Socket;
import io.ServerStorage;
import model.MessagesModel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ServerDaemon extends Thread {

    private int threadPoolTimeout;
    private int port;

    private ExecutorService threadPool;                             // The thread pool that will contain the requests handler threads
    private ServerSocket serverSocket;                              // The server socket on which the thread will be listening to
    private ServerStorage serverStorage;                            // Server storage
    private MessagesModel uiModel;                                  // User interface model

    public ServerDaemon(MessagesModel model) throws IOException {
        setDaemon(true);
        // Gets the port and nThreads number from the configuration file
        threadPoolTimeout = ConfigReader.getThreadPoolTimeout();
        port = ConfigReader.getPort();
        int nThreads = ConfigReader.getThreadsNo();

        this.uiModel = model;
        this.serverSocket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void run() {
        // ServerStorage initialization can be done on a separate thread (away from main thread) to improve performance
        uiModel.addMessage(Messages.INIT_STORAGE);
        this.serverStorage = new FilesManager();
        uiModel.addMessage(Messages.INIT_USERS_TABLE);
        UsersTable.init();
        uiModel.addMessage(String.format(Messages.SERVER_STARTED, port));
        try {
            while(!Thread.interrupted()) {
                Socket incomingConn = serverSocket.accept();
                // Create a new request handler thread passing to the constructor the incoming connection, the model and the serverStorage interface, then add it to the tread pool
                RequestHandler requestHandler = new RequestHandler(incomingConn, uiModel, serverStorage);
                threadPool.execute(requestHandler);
            }
        } catch (IOException e) {
            if(!(e instanceof SocketException && e.getMessage().equals("Socket closed"))) {
                e.printStackTrace();
                uiModel.addMessage(String.format(Messages.ERROR_MSG, e.getMessage()));
            }
        }
    }

    /**
     * Interrupt the thread.
     * In order to interrupt the server daemon, it firstly tries to wait for 10 seconds the requests to be served.
     * If the requests doesn't terminate after 10 seconds, a shutdownNow requests will be sent to the thread pool.
     */
    @Override
    public void interrupt() {
        uiModel.addMessage(Messages.SERVER_STOPPING);
        super.interrupt();
        try {   // try to stop the tasks on the thread pools
            threadPool.shutdown();
            if(!threadPool.awaitTermination(threadPoolTimeout, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
            serverSocket.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            uiModel.addMessage(String.format(Messages.ERROR_MSG, e.getMessage()));
        }
        uiModel.addMessage(Messages.SERVER_STOPPED);
    }

}
