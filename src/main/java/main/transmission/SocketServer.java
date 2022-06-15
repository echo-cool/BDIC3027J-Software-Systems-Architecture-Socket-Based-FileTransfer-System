package main.transmission;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketServer {
    private ServerSocket serverSocket;
    private ListeningThread listeningThread;
    private IMessageHandler IMessageHandler;

    public SocketServer(int port, IMessageHandler handler) {
        IMessageHandler = handler;
        try {
            serverSocket = new ServerSocket(port);
            listeningThread = new ListeningThread(this, serverSocket);
            listeningThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void setMessageHandler(IMessageHandler handler) {
        IMessageHandler = handler;
    }

    public IMessageHandler getMessageHandler() {
        return IMessageHandler;
    }
    
    /*
     * Ready for use.
     */
    public void close() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                listeningThread.stopRunning();
                listeningThread.suspend();
                listeningThread.stop();

                serverSocket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}